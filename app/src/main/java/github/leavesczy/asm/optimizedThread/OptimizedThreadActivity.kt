package github.leavesczy.asm.optimizedThread

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import github.leavesczy.asm.R
import java.util.concurrent.Executors

/**
 * @Author: leavesCZY
 * @Date: 2021/12/16 14:41
 * @Github: https://github.com/leavesCZY
 * @Desc:
 */
class OptimizedThreadActivity : AppCompatActivity() {

    private val btnSubmitTask by lazy {
        findViewById<Button>(R.id.btnSubmitTask)
    }

    private val btnAnonymousThread by lazy {
        findViewById<Button>(R.id.btnAnonymousThread)
    }

    private val tvLog by lazy {
        findViewById<TextView>(R.id.tvLog)
    }

    private val newFixedThreadPool = Executors.newFixedThreadPool(1)

    private val newSingleThreadExecutor = Executors.newSingleThreadExecutor()

    private val newCachedThreadPool = Executors.newCachedThreadPool()

    private val newSingleThreadScheduledExecutor = Executors.newSingleThreadScheduledExecutor()

    private val newScheduledThreadPool = Executors.newScheduledThreadPool(1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_optimized_thread)
        btnSubmitTask.setOnClickListener {
            val task: (String) -> Unit = { threadType ->
                Thread.sleep(600)
                val threadName = Thread.currentThread().name
                runOnUiThread {
                    tvLog.append("\n\n${threadType}: \n${threadName}")
                }
            }
            newFixedThreadPool.execute {
                task("newFixedThreadPool")
            }
            newSingleThreadExecutor.execute {
                task("newSingleThreadExecutor")
            }
            newCachedThreadPool.execute {
                task("newCachedThreadPool")
            }
            newSingleThreadScheduledExecutor.execute {
                task("newSingleThreadScheduledExecutor")
            }
            newScheduledThreadPool.execute {
                task("newScheduledThreadPool")
            }
            tvLog.append("\n********************")
        }
        btnAnonymousThread.setOnClickListener {
            Thread {
                Thread.sleep(600)
                val threadName = Thread.currentThread().name
                runOnUiThread {
                    tvLog.append("\n${threadName}")
                }
            }.start()
            tvLog.append("\n********************")
        }
    }

}