# Redis優惠券秒殺

---

## 全局唯一ID生成方法
### 1.UUID 2.Redis自增 3.snowflake算法 4.資料庫自增
![Redis優惠券秒殺_1.png](../../picture/Redis%E5%84%AA%E6%83%A0%E5%88%B8%E7%A7%92%E6%AE%BA_1.png)
![Redis優惠券秒殺.png](../../picture/Redis%E5%84%AA%E6%83%A0%E5%88%B8%E7%A7%92%E6%AE%BA.png)
[示範](../../../hm-dianping/src/main/java/com/hmdp/utils/RedisIdWorker.java)  
[測試ID自增量](../../../hm-dianping/src/test/java/com/hmdp/HmDianPingApplicationTests.java)

---