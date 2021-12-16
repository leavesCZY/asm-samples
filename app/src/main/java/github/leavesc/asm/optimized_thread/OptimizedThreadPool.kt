package github.leavesc.asm.optimized_thread

import java.util.concurrent.*

/**
 * @Author: leavesC
 * @Date: 2021/12/16 15:58
 * @Desc:
 */
class OptimizedThreadPool {

    companion object {

        @JvmStatic
        fun getExecutorService(threadSize: Int): ExecutorService {
            return getOptimizedExecutorService(threadSize = threadSize, name = "getExecutorService")
        }

        @JvmStatic
        fun getScheduledExecutorService(threadSize: Int, name: String): ExecutorService {
            return getOptimizedScheduledExecutorService(threadSize = threadSize, name = name)
        }

        private class NamedThreadFactory(private val name: String) : ThreadFactory {

            override fun newThread(runnable: Runnable): Thread {
                val thread = Thread(runnable)
                thread.name = "thread pool - $name"
                return thread
            }

        }

        private fun getOptimizedExecutorService(threadSize: Int, name: String): ExecutorService {
            val executor = ThreadPoolExecutor(
                threadSize, threadSize,
                10000L, TimeUnit.MILLISECONDS,
                LinkedBlockingQueue(),
                NamedThreadFactory(name)
            )
            executor.allowCoreThreadTimeOut(true)
            return executor
        }

        private fun getOptimizedScheduledExecutorService(
            threadSize: Int,
            name: String
        ): ExecutorService {
            val executor = ThreadPoolExecutor(
                threadSize, threadSize,
                10000L, TimeUnit.MILLISECONDS,
                LinkedBlockingQueue(),
                NamedThreadFactory(name)
            )
            executor.allowCoreThreadTimeOut(true)
            return executor
        }

    }

}