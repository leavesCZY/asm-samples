package github.leavesc.asm.plugins.optimized_thread

import org.objectweb.asm.Type

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
        methodName = "newFixedThreadPool"
    ),
    ThreadHookPoint(
        methodName = "newSingleThreadExecutor"
    ),
    ThreadHookPoint(
        methodName = "newCachedThreadPool"
    ),
    ThreadHookPoint(
        methodName = "newSingleThreadScheduledExecutor"
    ),
    ThreadHookPoint(
        methodName = "newScheduledThreadPool"
    ),
)

data class ThreadHookPoint(
    val methodName: String
) {

    fun getNewMethodDesc(methodDesc: String): String {
        val type = Type.getMethodType(methodDesc)
        val argumentTypes = type.argumentTypes
        val returnType = type.returnType
        val newArgumentTypes = arrayOfNulls<Type>(argumentTypes.size + 1)
        System.arraycopy(argumentTypes, 0, newArgumentTypes, 0, argumentTypes.size - 1 + 1)
        newArgumentTypes[newArgumentTypes.size - 1] = Type.getType(String::class.java)
        return Type.getMethodDescriptor(returnType, *newArgumentTypes)
    }

}