package github.leavesc.asm.utils

import java.util.concurrent.Executors

/**
 * @Author: leavesC
 * @Date: 2021/12/8 10:57
 * @Desc:
 */
object Log {

    private val logThreadExecutor = Executors.newSingleThreadExecutor()

    fun log(log: Any?) {
        logThreadExecutor.submit {
            println("ASM: $log")
        }
    }

}