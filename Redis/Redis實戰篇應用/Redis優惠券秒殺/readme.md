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
    @Transactional
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
        // 5. 扣減庫存
        boolean success = seckillVoucherService.update()
                .setSql("stock = stock - 1")
                .eq("voucher_id", voucherId).update();
        if (!success) {
            // 扣減失敗
            return Result.fail("庫存不足");
        }
        // 6. 創建訂單
        VoucherOrder voucherOrder = new VoucherOrder();
        // 6.1 訂單 id
        Long orderId = redisIsIdWorker.nextId("order");
        voucherOrder.setId(orderId);
        // 6.2 用戶 id
        Long userId = UserHolder.getUser().getId();
        voucherOrder.setUserId(userId);
        // 6.3 代金券 id
        voucherOrder.setVoucherId(voucherId);
        save(voucherOrder);

        // 7. 返回訂單id
        return Result.ok(orderId);
    }
}
```