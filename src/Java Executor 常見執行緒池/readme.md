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

### 📌 範例
```java
// Runnable
Runnable task1 = () -> System.out.println("Runnable 任務執行");
new Thread(task1).start();

// Callable
Callable<Integer> task2 = () -> 1 + 2 + 3;
Future<Integer> future = Executors.newSingleThreadExecutor().submit(task2);
System.out.println("結果: " + future.get());
