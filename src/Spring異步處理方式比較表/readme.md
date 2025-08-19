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
