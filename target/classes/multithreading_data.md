#### 1. Modern Java: Virtual Threads & Loom (虚拟线程与Loom)
*   **针对问题**：Java 21+ 新特性。涉及虚拟线程的性能、钉住（Pinning）、与旧代码的兼容性。
*   **Regex**: `(?i).*(virtual.?thread|structured.?concurrency|carrier.?thread|platform.?thread|project.?loom|scoped.?value).*`

#### 2. UI & Event Dispatch Thread (UI与事件分发线程)
*   **针对问题**：JavaFX, Swing, Android。核心痛点是“界面卡死”或“非UI线程更新UI”。
*   **Regex**: `(?i).*(javafx|swing|android|awt|event.?dispatch|ui.?thread|gui|progress.?bar|update.?ui|background.?task).*`

#### 3. Thread Lifecycle: Interruption & Shutdown (生命周期：中断与关闭)
*   **针对问题**：线程关不掉、`InterruptedException` 处理、守护线程（Daemon）、优雅停机。这是极其高频的痛点。
*   **Regex**: `(?i).*(interrupt|shutdown|stop|cancel|terminate|kill|die|linger|abort|close|exit|uninterruptible).*`

#### 4. Thread Pool Configuration & Scheduling (线程池配置与调度)
*   **针对问题**：`ThreadPoolExecutor` 参数设置、拒绝策略（Reject）、定时任务、池大小（Sizing）。
*   **Regex**: `(?i).*(thread.?pool|executor|scheduled|core.?pool|max.?pool|queue.?capacity|reject|submit|execute|new.?fixed|cached.?pool|work.?stealing).*`

#### 5. Framework Context & Propagation (框架上下文与传播)
*   **针对问题**：Spring/Hibernate/Web 环境下，`ThreadLocal` 丢失、事务失效、Scope 问题、ORM 会话关闭。
*   **Regex**: `(?i).*(spring|hibernate|transaction|jpa|entity.?manager|servlet|tomcat|request.?scope|security.?context|bean|autowired|controller|service).*`

#### 6. Async Patterns: Futures & Promises (异步编排与Future)
*   **针对问题**：`CompletableFuture` 的链式调用、异常处理、回调地狱、`join()`/`get()` 阻塞。
*   **Regex**: `(?i).*(completable.?future|future|supply.?async|then.?apply|promise|callback|listenable|asynchronous|reactive|non.?blocking).*`

#### 7. Parallel Streams & Data Processing (并行流与数据处理)
*   **针对问题**：Java 8 `parallelStream` 的陷阱、Common Pool 争用、大数据量并行计算。
*   **Regex**: `(?i).*(parallel.?stream|stream.?api|map.?reduce|fork.?join|split|aggregate|batch|process.*data|large.?number).*`

#### 8. Thread Safe Collections & State (线程安全集合与状态)
*   **针对问题**：`HashMap` vs `ConcurrentHashMap`，`ArrayList` 的并发修改异常，队列（Queue）的使用。
*   **Regex**: `(?i).*(list|map|set|queue|stack|collection|array|concurrent.?mod|iterator|blocking.?queue|copy.?on.?write).*`

#### 9. Synchronization Primitives & Locking (锁与同步原语)
*   **针对问题**：底层机制。`synchronized` 关键字、`ReentrantLock`、`volatile`、原子类（Atomic）、死锁、竞态条件。
*   **Regex**: `(?i).*(lock|synchroniz|atomic|volatile|mutex|monitor|condition|wait|notify|signal|deadlock|race.?condition|barrier|latch|semaphore|phaser).*`

#### 10. Memory Model & Visibility (内存模型与可见性)
*   **针对问题**：JMM、Happens-before原则、指令重排、缓存一致性（较深层且隐晦的问题）。
*   **Regex**: `(?i).*(memory.?model|visibility|happens.?before|instruction.?reorder|cache|cpu.?register|fence|barrier).*`

#### 11. Testing, Mocking & Debugging (测试与调试)
*   **针对问题**：单元测试中的并发、Mock 框架失效、断言失败、日志乱序。
*   **Regex**: `(?i).*(test|junit|mock|assert|verify|debug|log|print|output|reproduc|console).*`

#### 12. Performance & Resource Contention (性能与资源争用)
*   **针对问题**：CPU 100%、上下文切换过高、吞吐量低、内存泄漏（Leak）。
*   **Regex**: `(?i).*(perform|slow|latency|throughput|cpu|memory|leak|overhead|starvation|scale|efficien|optimi|bottleneck).*`
