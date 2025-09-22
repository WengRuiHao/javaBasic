# Redis學習

--- 

## [Redis 基礎指令篇](Redis基礎指令篇)
## Java Redis Client 比較
| Client             | 說明                                                                                          | 特點                                                                                         |
|--------------------|---------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------|
| **Jedis**          | 以 Redis 命令作為方法名稱，學習成本低，簡單實用。                                              | 單執行緒**不安全**，多執行緒環境下需基於連接池使用。                                          |
| **Lettuce**        | 基於 **Netty** 實現，支援同步、非同步和響應式編程方式，並且是**執行緒安全**的。                  | 支援 Redis 的哨兵模式、集群模式和管道模式。                                                  |
| **Redisson**       | 基於 Redis 實現的分布式、可伸縮的 Java 資料結構集合。                                          | 包含 Map、Queue、Lock、Semaphore、AtomicLong 等強大功能。                                    |
在 Spring Data Redis 裡，官方推薦的主要是 **Jedis** 和 **Lettuce**。

---

[**JAVA Redis 使用測試**](src/test/java/com/ruihao/test/JedisTest.java)  
[**連接池配置**](src/main/java/com/ruihao/jedis/util/JedisConnectionFactory.java)
![連接池配置](picture/RedisPool.png)

