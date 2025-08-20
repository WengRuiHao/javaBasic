# 🧵 Java Concurrency 學習筆記

本筆記整理了 Java 多執行緒與 Executor Framework 的基礎重點與實務應用，方便學習與快速複習。

---

## 📌 1. 為什麼要用 ExecutorService？

### ❌ new Thread 的問題
- 每次都要建立與銷毀 Thread，成本高。
- 無法控制 Thread 數量，容易 OOM。
- 無法方便管理任務（取消、超時、回傳值）。

### ✅ ExecutorService 的優勢
- **執行緒池 (Thread Pool)**：執行緒可重複利用，節省資源。
- **任務管理**：`submit()` 可回傳 `Future`，取得結果或取消任務。
- **彈性策略**：支援固定池、快取池、單執行緒池、排程池。
- **可擴充**：`ThreadPoolExecutor` 可自訂參數與拒絕策略。

---

## 📌 2. 常見執行緒池類型比較

| 類型 | 建立方式 | 特點 | 適用場景 | 風險/缺點 |
|------|---------|------|----------|-----------|
| **FixedThreadPool** | `Executors.newFixedThreadPool(n)` | 固定 n 條執行緒，任務多時進入佇列 | CPU 密集型工作（計算、壓縮） | 任務暴增時佇列可能塞滿記憶體 |
| **CachedThreadPool** | `Executors.newCachedThreadPool()` | 執行緒數動態增減，空閒 60 秒回收 | I/O 密集型、大量短任務 | 任務太多可能建立過多執行緒，壓垮系統 |
| **SingleThreadExecutor** | `Executors.newSingleThreadExecutor()` | 單一執行緒，順序執行任務 | 日誌、訂單處理（需順序性） | 效能低，單點故障 |
| **ScheduledThreadPool** | `Executors.newScheduledThreadPool(n)` | 支援延遲與週期任務 | 定時排程、心跳檢查 | 任務太長可能導致排程不準 |
| **WorkStealingPool** | `Executors.newWorkStealingPool()` | ForkJoinPool，支援任務平衡 | 平行運算、大量小任務 | 任務不可依賴執行順序 |

---

## 📌 3. Runnable vs Callable

| 特性 | Runnable | Callable |
|------|----------|----------|
| 方法 | `void run()` | `V call()` |
| 回傳值 | 無 | 有 |
| Checked Exception | 不可丟 | 可丟 |
| 使用方式 | `new Thread(runnable).start()`<br>`executor.execute(runnable)` | `executor.submit(callable)`，回傳 `Future` |

## 📌 Callable實務應用場景
**1️⃣ 平行計算 → 等結果匯總**  
場景：查詢報表，需要同時從「訂單系統」、「物流系統」、「支付系統」拿資料，最後合併。
```java
Future<Order> order = pool.submit(this::getOrder);
Future<Logistics> logistics = pool.submit(this::getLogistics);
Future<Payment> payment = pool.submit(this::getPayment);

OrderReport report = new OrderReport(order.get(), logistics.get(), payment.get());
```
👉 `future.get()` 會阻塞，直到任務完成，確保資料一致性。

--- 

**2️⃣ API 呼叫 → 限時等待**  
場景：呼叫外部 API（例如支付系統），不允許無限等待。
```java
Future<String> apiFuture = pool.submit(() -> callExternalAPI());

try {
    String response = apiFuture.get(2, TimeUnit.SECONDS); // 最多等 2 秒
    System.out.println("API 回應: " + response);
} catch (TimeoutException e) {
    System.out.println("超時，走預設邏輯");
}
```
👉 避免外部系統卡死，保護自己服務。

---

**3️⃣ 使用者取消長任務**
場景：使用者查詢大報表，但中途關閉頁面 → 沒必要繼續計算。
```java
Future<?> reportTask = pool.submit(this::generateReport);

// 使用者取消操作時
reportTask.cancel(true);
```
👉 節省系統資源，避免做白工。

---

**4️⃣ 批次處理 → 等待全部完成**
場景：批次下載檔案，全部完成後再壓縮。
```java
List<Future<File>> futures = urls.stream()
    .map(url -> pool.submit(() -> downloadFile(url)))
    .toList();

List<File> files = new ArrayList<>();
for (Future<File> f : futures) {
    files.add(f.get()); // 阻塞等待
}

zipFiles(files); // 全部完成後壓縮
```
👉 確保所有子任務完成後，才能執行後續流程。

---

## 📌 Future 小結

| 場景       | 用法                        | 說明                                     |
|------------|-----------------------------|------------------------------------------|
| 平行計算   | `future.get()`              | 阻塞等待，合併多個結果                   |
| API 呼叫   | `future.get(timeout, unit)` | 限時等待，超時丟 `TimeoutException`     |
| 使用者取消 | `future.cancel(true)`       | 中斷長任務，避免浪費資源                 |
| 批次處理   | 多個 Future + `get()`       | 等全部子任務完成，再做後續處理           |

