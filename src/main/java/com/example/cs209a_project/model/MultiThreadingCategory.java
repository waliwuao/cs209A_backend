package com.example.cs209a_project.model;

import org.jsoup.Jsoup;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum MultiThreadingCategory {

    // 1. [Specific] 虚拟线程 (Java 21+) - 特征非常明显，保持原样
    VIRTUAL_THREADS(
            Pattern.compile("(?i)(virtual\\s*thread|structured\\s*concurrency|project\\s*loom|scoped\\s*value|virtualthread)")
    ),

    // 2. [Context] UI 线程交互 - 必须包含 "UI" 和 "线程/异步" 的强关联词
    UI_EVENT_DISPATCH(
            Pattern.compile("(?i)(event\\s*dispatch\\s*thread|edt\\b|swingworker|swingutilities\\.invoke|android\\.os\\.handler|looper|asynctask|runonuithread|platform\\.runlater|task\\s*service\\s*javafx|background\\s*task\\s*ui|main\\s*thread\\s*freeze|anr\\b)")
    ),

    // 3. [Context] 框架并发 (Spring/Web) - 必须包含 @Async, 事务传播, 或请求上下文
    FRAMEWORK_CONTEXT(
            Pattern.compile("(?i)(@Async|enableasync|threadpooltaskexecutor|taskexecutor|requestcontextholder|threadlocalscoped|opensessioninview|lazyinitializationexception|propagation\\.requires_new|concurrent\\s*transaction|spring\\s*security\\s*context)")
    ),

    // 4. [API] 并行流 - 增加 parallelStream 的准确度
    PARALLEL_STREAMS(
            Pattern.compile("(?i)(parallelstream|stream\\.parallel|forkjoinpool\\.commonpool|spliterator|parallel\\s*processing\\s*stream)")
    ),

    // 5. [API] 异步编排 - 锁定 CompletableFuture 及其相关类
    ASYNC_FUTURES(
            Pattern.compile("(?i)(completablefuture|completionstage|supplyasync|runasync|thenapply|thencompose|listenablefuture|futuretask|java\\.util\\.concurrent\\.future)")
    ),

    // 6. [API] 线程池 - 排除数据库连接池，锁定执行器
    THREAD_POOL(
            Pattern.compile("(?i)(threadpoolexecutor|executorservice|scheduledexecutorservice|newfixedthreadpool|newcachedthreadpool|workstealingpool|rejectedexecutionhandler|corepoolsize|maxpoolsize|blockingqueue\\s*capacity)")
    ),

    // 7. [Data] 线程安全集合 - 精确匹配类名
    THREAD_SAFE_COLLECTIONS(
            Pattern.compile("(?i)(concurrenthashmap|copyonwrite|concurrentlinked|blockingqueue|arrayblockingqueue|linkedblockingqueue|priorityblockingqueue|delayqueue|synchronousqueue|collections\\.synchronized)")
    ),

    // 8. [Lifecycle] 线程生命周期 - 聚焦中断、守护和死循环
    LIFECYCLE_CONTROL(
            Pattern.compile("(?i)(thread\\.interrupt|interruptedexception|thread\\.join|thread\\.yield|thread\\.sleep|daemon\\s*thread|shutdownhook|graceful\\s*shutdown|runaway\\s*thread|thread\\s*stuck)")
    ),

    // 9. [Mechanism] 锁与同步 - 排除数据库锁，聚焦 Java 锁机制
    SYNCHRONIZATION_LOCKING(
            Pattern.compile("(?i)(synchronized\\s*\\(|reentrantlock|reentrantreadwritelock|stampedlock|condition\\.await|object\\.wait|object\\.notify|deadlock\\s*detected|thread\\s*dump|monitor\\s*lock|illegalmonitorstate)")
    ),

    // 10. [Mechanism] 内存模型 (JMM) - 聚焦可见性与重排序
    MEMORY_MODEL_JMM(
            Pattern.compile("(?i)(volatile\\b|happens-before|memory\\s*barrier|instruction\\s*reordering|visibility\\s*guarantee|jmm\\b|double\\s*checked\\s*locking|atomicreference|atomicinteger|longadder)")
    ),

    // 11. [Practice] 并发测试 - 必须是测试工具 + 并发场景
    CONCURRENT_TESTING(
            Pattern.compile("(?i)(awaitility|concurrentunittest|multithreadedtest|countDownLatch\\s*test|thread\\s*safety\\s*test|race\\s*condition\\s*reproduc|mock.*async|verify.*timeout)")
    ),

    // 12. [Issue] 性能与资源争用 - 只有明确提到上下文切换或饥饿才算
    PERFORMANCE_CONTENTION(
            Pattern.compile("(?i)(context\\s*switch|thread\\s*starvation|thread\\s*contention|cpu\\s*thrashing|thread\\s*leak|out\\s*of\\s*memory\\s*unable\\s*to\\s*create\\s*thread|high\\s*concurrency\\s*performance)")
    );

    private final Pattern pattern;

    MultiThreadingCategory(Pattern pattern) {
        this.pattern = pattern;
    }

    public static MultiThreadingCategory classify(String rawText) {
        if (rawText == null || rawText.isEmpty()) return null;

        String text = Jsoup.parse(rawText).text();

        for (MultiThreadingCategory category : values()) {
            Matcher matcher = category.pattern.matcher(text);
            if (matcher.find()) {
                return category; // 命中即返回，配合 Enum 定义顺序的优先级
            }
        }
        return null;
    }
}