package github.leavesc.asm.optimized_thread

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import github.leavesc.asm.R
import java.util.concurrent.Executors

/**
 * @Author: leavesC
 * @Date: 2021/12/16 14:41
 * @Github: https://github.com/leavesC
 * @Desc:
 */
class OptimizedThreadActivity : AppCompatActivity() {

    private val tvLog by lazy {
        findViewById<TextView>(R.id.tvLog)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_optimized_thread)
        val threadPool = Executors.newFixedThreadPool(10)
        threadPool.submit {
            val threadName = Thread.currentThread().name
            runOnUiThread {
                tvLog.text = threadName
            }
        }
    }

}