package github.leavesc.asm.optimized_thread

import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger

/**
 * @Author: leavesC
 * @Date: 2021/12/16 15:58
 * @Desc:
 */
class OptimizedThreadPool {

    companion object {

        @JvmStatic
        fun getExecutorServiceWithName(threadSize: Int, className: String): ExecutorService {
            return getOptimizedExecutorService(
                threadSize = threadSize,
                name = className
            )
        }

        @JvmStatic
        fun getScheduledExecutorServiceWithName(
            threadSize: Int,
            className: String
        ): ScheduledExecutorService {
            return getOptimizedScheduledExecutorService(
                threadSize = threadSize,
                name = className
            )
        }

        private class NamedThreadFactory(private val name: String) : ThreadFactory {

            private val threadId = AtomicInteger(0)

            override fun newThread(runnable: Runnable): Thread {
                val thread = Thread(runnable)
                thread.name = name + " - " + threadId.getAndIncrement()
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
        ): ScheduledExecutorService {
            return Executors.newScheduledThreadPool(threadSize, NamedThreadFactory(name))
        }

    }

}