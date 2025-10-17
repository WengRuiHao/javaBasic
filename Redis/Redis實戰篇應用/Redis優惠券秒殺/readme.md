# Redis優惠券秒殺

---

## 全局唯一ID生成方法
### 1.UUID 2.Redis自增 3.snowflake算法 4.資料庫自增
![Redis優惠券秒殺_1.png](../../picture/Redis%E5%84%AA%E6%83%A0%E5%88%B8%E7%A7%92%E6%AE%BA_1.png)
![Redis優惠券秒殺.png](../../picture/Redis%E5%84%AA%E6%83%A0%E5%88%B8%E7%A7%92%E6%AE%BA.png)
[示範](../../../hm-dianping/src/main/java/com/hmdp/utils/RedisIsIdWorker.java)  
[測試ID自增量](../../../hm-dianping/src/test/java/com/hmdp/HmDianPingApplicationTests.java)

---

## 實現優惠券秒殺下單
![Redis優惠券秒殺_3.png](../../picture/Redis%E5%84%AA%E6%83%A0%E5%88%B8%E7%A7%92%E6%AE%BA_3.png)
![Redis優惠券秒殺_2.png](../../picture/Redis%E5%84%AA%E6%83%A0%E5%88%B8%E7%A7%92%E6%AE%BA_2.png)
```java
@Service
public class VoucherOrderServiceImpl extends ServiceImpl<VoucherOrderMapper, VoucherOrder> implements IVoucherOrderService {

    @Autowired
    private ISeckillVoucherService seckillVoucherService;

    @Autowired
    private RedisIsIdWorker redisIsIdWorker;

    @Override
    public Result seckillVoucher(Long voucherId) {
        // 1. 查詢優惠券
        SeckillVoucher voucher = seckillVoucherService.getById(voucherId);
        // 2. 判斷秒殺是否開始
        if (voucher.getBeginTime().isAfter(LocalDateTime.now())) {
            // 尚未開始
            return Result.fail("秒殺尚未開始");
        }
        // 3. 判斷秒殺是否已經結束
        if (voucher.getEndTime().isBefore(LocalDateTime.now())) {
            // 已經結束
            return Result.fail("秒殺已經結束");
        }
        // 4. 判斷庫存是否充足
        if (voucher.getStock() < 1) {
            // 庫存不足
            return Result.fail("庫存不足");
        }

        Long userId = UserHolder.getUser().getId();
        synchronized (userId.toString().intern()) {
            // 獲取代理對象(事務)
            IVoucherOrderService proxy = (IVoucherOrderService) AopContext.currentProxy();
            return proxy.createVoucherOrder(voucherId);
        }
    }

    @Transactional
    public Result createVoucherOrder(Long voucherId) {
        // 5. 一人一單
        Long userId = UserHolder.getUser().getId();

        // 5.1. 查詢訂單
        int count = query().eq("user_id", userId).eq("voucher_id", voucherId).count();
        // 5.2. 判斷是否存在
        if (count > 0) {
            // 用戶已經購買過了
            return Result.fail("用戶已經購買過一次!");
        }

        // 6. 扣減庫存
        // 解決超賣問題
        boolean success = seckillVoucherService.update()
                .setSql("stock = stock - 1") // set stock = stock - 1
                .eq("voucher_id", voucherId).gt("stock", 0) // where id = ? and stock > 0
                .update();
        if (!success) {
            // 扣減失敗
            return Result.fail("庫存不足");
        }

        // 7. 創建訂單
        VoucherOrder voucherOrder = new VoucherOrder();
        // 7.1 訂單 id
        Long orderId = redisIsIdWorker.nextId("order");
        voucherOrder.setId(orderId);
        // 7.2 用戶 id
        voucherOrder.setUserId(userId);
        // 7.3 代金券 id
        voucherOrder.setVoucherId(voucherId);
        save(voucherOrder);

        // 8. 返回訂單id
        return Result.ok(orderId);
    }
}
```

---

## 超賣問題
![Redis優惠券秒殺_4.png](../../picture/Redis%E5%84%AA%E6%83%A0%E5%88%B8%E7%A7%92%E6%AE%BA_4.png)
![Redis優惠券秒殺_5.png](../../picture/Redis%E5%84%AA%E6%83%A0%E5%88%B8%E7%A7%92%E6%AE%BA_5.png)
![Redis優惠券秒殺_6.png](../../picture/Redis%E5%84%AA%E6%83%A0%E5%88%B8%E7%A7%92%E6%AE%BA_6.png)
![Redis優惠券秒殺_7.png](../../picture/Redis%E5%84%AA%E6%83%A0%E5%88%B8%E7%A7%92%E6%AE%BA_7.png)

---

## 一人一單
![Redis優惠券秒殺_8.png](../../picture/Redis%E5%84%AA%E6%83%A0%E5%88%B8%E7%A7%92%E6%AE%BA_8.png)
![Redis優惠券秒殺_9.png](../../picture/Redis%E5%84%AA%E6%83%A0%E5%88%B8%E7%A7%92%E6%AE%BA_9.png)
![Redis優惠券秒殺_10.png](../../picture/Redis%E5%84%AA%E6%83%A0%E5%88%B8%E7%A7%92%E6%AE%BA_10.png)

---