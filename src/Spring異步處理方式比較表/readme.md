# 📖 Spring 異步處理方式比較表

Spring 提供多種異步與排程處理方式，以下整理各種技術的用途、特點、適合場景與備註，方便快速選擇。

---

## 📊 技術比較

| 技術 | 用途 | 特點 | 適合場景 | 備註 |
|------|------|------|----------|------|
| **@Async** | 最簡單的非同步呼叫 | 背後用 `TaskExecutor`（預設 `SimpleAsyncTaskExecutor`）；不會阻塞主執行緒 | 寄信、發通知、寫 log、打外部 API | 搭配 `@EnableAsync` 使用；可改配置執行緒池 |
| **@Async + CompletableFuture** | 非同步呼叫 + 需要回傳結果 | 支援回傳結果，像 JS 的 `Promise` | 查詢外部服務後回傳結果、並行任務聚合 | 可用 `.get()` 或 `.thenApply()` 拿結果 |
| **@Scheduled** | 簡單定時排程 | 輕量級，支援 cron 表達式 | 每天清理資料、固定時間寄報表 | 搭配 `@EnableScheduling` 使用 |
| **Quartz** | 複雜定時排程 | 任務可持久化到 DB；支援動態新增/刪除任務 | 大型專案定時任務（金融報表、批次通知） | 功能強大，但配置比 `@Scheduled` 複雜 |
| **Spring Batch** | 大規模資料批次處理 | 支援 chunk 分批處理、retry、skip；有 job/step 概念 | 每晚處理百萬級訂單、金融對帳 | 常與 Quartz 或 Scheduler 搭配使用 |
| **消息佇列 (Kafka / RabbitMQ / ActiveMQ)** | 異步解耦 | 透過 MQ 發送消息，消費者獨立處理 | 訂單系統、通知系統、跨服務事件傳遞 | 離開 Spring 本身，但最常見於大型系統 |

---

## 🎯 技術選擇建議

- **輕量異步** → `@Async`
- **異步 + 需要結果** → `@Async + CompletableFuture`
- **簡單定時任務** → `@Scheduled`
- **複雜定時任務（持久化、動態管理）** → Quartz
- **大量資料批次處理** → Spring Batch
- **跨系統事件傳遞 / 異步解耦** → 消息佇列（Kafka / RabbitMQ / ActiveMQ）

---
## 🔹 1. `@Async`（Spring 最基本的異步方法）
👉 適合：簡單的「非同步呼叫」，例如寄信、打 API、寫 log。
- **原理：**
  - Spring 在背景幫你建立一個 ThreadPoolTaskExecutor（執行緒池）。
  - 你在方法上加 @Async → Spring 會把這個方法丟到執行緒池執行，而不是阻塞當前主執行緒。
- **使用方式：**
```dbn-psql
@Service
public class MailService {
    @Async
    public void sendEmail(String user) {
        System.out.println("寄送信件給 " + user + " 由 " + Thread.currentThread().getName());
    }
}
```
```dbn-psql
@SpringBootApplication
@EnableAsync // 開啟 @Async 支援
public class DemoApplication {}
```
- **效果：**
  - 呼叫`mailService.sendEmail("小明")`時，會在背景執行，不會卡住主線程。
## 🔹 2. `CompletableFuture` + `@Async`
👉 適合：需要拿到非同步任務「回傳結果」的情境。
- **為什麼要搭配 `CompletableFuture`？**
  - `@Async` 預設回傳 `void，你不知道任務什麼時候結束。
  - 如果方法回傳 `CompletableFuture<T>`，就可以用 `.get()` 或 `.thenApply()` 拿到結果。
- **範例:**
```dbn-psql
@Service
public class UserService {
    @Async
    public CompletableFuture<String> findUser(String id) throws InterruptedException {
        Thread.sleep(2000);
        return CompletableFuture.completedFuture("User-" + id);
    }
}
```
```dbn-psql
CompletableFuture<String> result = userService.findUser("123");
result.thenAccept(user -> System.out.println("查到使用者：" + user));
```