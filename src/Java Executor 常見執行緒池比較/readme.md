# 🧵 Java Executor 常見執行緒池比較

## 類型比較表

| 類型 | 建立方式 | 特點 | 適用場景 | 風險/缺點 |
|------|---------|------|----------|-----------|
| **FixedThreadPool** | `Executors.newFixedThreadPool(n)` | - 固定 n 條執行緒<br>- 任務多於執行緒時進入佇列 | - **CPU 密集型工作**（例如演算法計算）<br>- 任務數量可預估且穩定 | - 如果任務量暴增，佇列可能塞滿記憶體 |
| **CachedThreadPool** | `Executors.newCachedThreadPool()` | - 執行緒數不固定，會動態擴張<br>- 空閒執行緒逾時 60 秒會回收 | - **IO 密集型任務**（等待時間長）<br>- 任務數量不固定、短小快速任務 | - 任務太多可能建立大量執行緒 → **壓垮 CPU/記憶體** |
| **SingleThreadExecutor** | `Executors.newSingleThreadExecutor()` | - 只有一條執行緒<br>- 保證任務按提交順序執行 | - 需要 **順序執行** 的情境<br>- 日誌寫入、訂單處理 | - 效能最低，單點故障（若執行緒掛掉就重建，但仍只有一條） |
| **ScheduledThreadPool** | `Executors.newScheduledThreadPool(n)` | - 支援 **延遲任務** 與 **週期性任務** | - 定時排程：例如心跳檢查、監控、排程任務 | - 任務執行時間過長可能導致排程不準 |
| **WorkStealingPool** (Java 8+) | `Executors.newWorkStealingPool()` | - 使用 ForkJoinPool<br>- 任務會被空閒執行緒「偷走」平衡負載 | - **大批量小任務**、平行運算（divide & conquer） | - 任務不可依賴執行順序 |

---

## 📌 小結建議

- **CPU 密集型工作**（例如壓縮、加解密、計算）：👉 `FixedThreadPool`（核心數 ≈ CPU 核心數）
- **IO 密集型工作**（網路請求、資料庫操作）：👉 `CachedThreadPool`（彈性較好）
- **需要順序性**：👉 `SingleThreadExecutor`
- **定時/週期性任務**：👉 `ScheduledThreadPool`
- **平行計算 / 大量小任務**：👉 `WorkStealingPool`

---
