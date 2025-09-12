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
