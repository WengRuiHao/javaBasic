# Java 常用併發設計處理方式

## 1. 生產者–消費者模式 (Producer–Consumer)
**核心工具**：`BlockingQueue`

- **用途**：解耦「任務的產生」與「任務的處理」
- **特點**：
    - 滿了 → `put()` 會阻塞
    - 空了 → `take()` 會阻塞
- **常見場景**：
    - 日誌系統
    - 訂單處理
    - 消息佇列 (Message Queue)

## 2. 工作竊取 (Work Stealing)

**核心工具**：`ForkJoinPool`

**用途**：提升 CPU 利用率，避免部分執行緒閒置

**特點**：
- 每個執行緒有自己的任務佇列
- 閒的執行緒會「偷」別人的任務

**常見場景**：
- 平行計算（大數據、影像處理）
- Java Stream `.parallel()`

## 3. Future + CompletionService

**核心工具**：`ExecutorCompletionService`

**用途**：批量任務處理，支援「先完成的先取結果」

**特點**：
- 傳統 `Future` → 按提交順序取結果，可能卡住
- `CompletionService` → 先完成的先取出，效率更高

**常見場景**：
- 多 API 請求並行，先返回的先處理
- 網路爬蟲，多任務下載  

## 4. Atomic 類 (無鎖計數器)

**核心工具**：`AtomicInteger`、`AtomicLong`、`AtomicReference`

**用途**：實現「無鎖」的數值操作，避免 `synchronized` 開銷

**常見場景**：
- 計數器（如網站訪問量）
- 統計任務數量  

## 5. 併發集合
**核心工具**：`ConcurrentHashMap`、`CopyOnWriteArrayList`

**用途**：多執行緒下安全存取集合

**常見場景**：
- 緩存表（快取）
- 多任務共享資料結構