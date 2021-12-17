package github.leavesc.asm.plugins.optimized_thread

/**
 * @Author: leavesC
 * @Date: 2021/12/17 11:02
 * @Desc:
 */
class OptimizedThreadConfig(
    val executorsClass: String = "java/util/concurrent/Executors",
    private val optimizedThreadPoolClass: String = "github.leavesc.asm.optimized_thread.OptimizedThreadPool",
    val threadHookPointList: List<ThreadHookPoint> = threadHookPoints,
) {

    val formatOptimizedThreadPoolClass: String
        get() = optimizedThreadPoolClass.replace(".", "/")

}

private val threadHookPoints = listOf(
    ThreadHookPoint(
        methodName = "newFixedThreadPool",
        methodDesc = "(I)Ljava/util/concurrent/ExecutorService;",
        methodNameReplace = "getExecutorService"
    ),
    ThreadHookPoint(
        methodName = "newScheduledThreadPool",
        methodDesc = "(I)Ljava/util/concurrent/ScheduledExecutorService;",
        methodNameReplace = "getScheduledExecutorService"
    )
)

data class ThreadHookPoint(
    val methodName: String,
    val methodDesc: String,
    val methodNameReplace: String
)