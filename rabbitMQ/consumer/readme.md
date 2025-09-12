# Spring RabbitMQ 消費者配置介紹

在 Spring 中使用 RabbitMQ 作為消息隊列時，可以通過 `application.yml` 或 `application.properties` 配置 RabbitMQ 連接的相關參數。以下是常見的配置示範：

## 配置範例
### [配置檔位置](src/main/resources)
### 使用 `application.yml`

```yaml
spring:
  rabbitmq:
    host: localhost          # RabbitMQ 服務的主機地址
    port: 5672               # RabbitMQ 服務的端口（默認為 5672）
    virtual-host: /          # RabbitMQ 的虛擬主機，通常默認為 '/'
    username: ruihaoweng     # RabbitMQ 服務的使用者名稱
    password: **********     # RabbitMQ 服務的密碼
```
### Spring AMQP如何收到消息
- 1.引入spring-boot-start-amqp依賴
- 2.RabbitMQ配置 服務端訊息
- 3.利用 `@RabbitListener` 註解宣告要監聽的queue，監聽消息  
 [範例](src/main/java/com/javaBasic/listeners/MQListener.java)  
  
### 如果遇到綁定多個消費者 
他會輪詢給每個消費者消息，但他不會去考慮消費者是否處理完消息，就可能會發生***消息堆疊的問題***   
處理方法: 修改***配置檔案***加上`prefetch`
![處理方法](consumer.png)