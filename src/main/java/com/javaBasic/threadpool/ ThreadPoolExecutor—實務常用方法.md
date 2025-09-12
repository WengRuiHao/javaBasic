# 🧵 ThreadPoolExecutor — 實務常用方法

---

## 1️⃣ 提交任務

- `execute(Runnable task)`  
  提交一個 **不需要回傳結果** 的任務。

- `submit(Runnable/Callable task)`  
  提交一個任務，會回傳 `Future`，可用來 **取得結果 / 取消 / 阻塞等待**。

👉 **實務上**：大部分會用 `submit`，因為更靈活。

---

## 2️⃣ 監控方法（很常用，寫監控介面或 debug）

- `getPoolSize()` → 目前池子裡有幾條執行緒（包括閒置 + 工作中）。
- `getActiveCount()` → 目前有幾條執行緒正在執行任務。
- `getQueue().size()` → 工作佇列裡還積壓多少任務。
- `getCompletedTaskCount()` → 已經完成的任務數。
- `getTaskCount()` → 曾經提交過的任務總數。

👉 **實務上**：常用在 **監控平台（Prometheus / Grafana）** 或 log 輸出，幫助排查瓶頸。

---

## 3️⃣ 控制方法

- `shutdown()`  
  平滑關閉：不接受新任務，但會執行完 queue 裡的任務。

- `shutdownNow()`  
  嘗試立即停止：中斷正在執行的任務，返回尚未執行的任務清單。

- `allowCoreThreadTimeOut(true)`  
  讓核心執行緒在閒置時也能被回收（預設核心執行緒不會回收）。

👉 **實務上**：
- 多數用 `shutdown()`，例如應用程式要關閉時。
- `shutdownNow()` 很少用，因為有風險（會打斷任務）。

---

## 4️⃣ 自訂配置（通常在建構時）

- **workQueue**
    - `ArrayBlockingQueue`
    - `LinkedBlockingQueue`
    - `SynchronousQueue`

- **RejectedExecutionHandler**（拒絕策略）
    - `AbortPolicy`（預設，丟例外）
    - `CallerRunsPolicy`（呼叫端執行）
    - `DiscardPolicy`（直接丟掉）
    - `DiscardOldestPolicy`（丟掉最舊任務再嘗試提交）

👉 **實務上**：幾乎所有專案都會自己設定 **queue** 與 **拒絕策略**，避免用 `Executors` 預設的無界 queue。

---

## 📊 實務上最常用的場景

### ✅ 任務提交
- `submit()` 拿 `Future` → 拿結果或加 timeout。

### ✅ 監控
- `getActiveCount()` 看目前忙碌程度。
- `getQueue().size()` 看有沒有任務堆積。

### ✅ 控制
- `shutdown()` 在服務下線時平滑釋放資源。

---

## ✅ 總結

- **ThreadPoolExecutor 的重點** → 概念 + 配置（核心數、最大數、queue、拒絕策略）。
- **實務上常用的方法**：
    - 提交：`submit()`
    - 監控：`getActiveCount()`、`getQueue().size()`
    - 控制：`shutdown()`、`shutdownNow()`

其他方法（例如 `prestartAllCoreThreads()`、`setCorePoolSize()`）偏進階，除非做框架或效能調優，不會天天用。  
