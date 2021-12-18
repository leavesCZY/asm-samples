package github.leavesc.asm.optimized_thread

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import github.leavesc.asm.R
import github.leavesc.asm.double_click.UncheckViewOnClick
import java.util.concurrent.Executors
import kotlin.random.Random

/**
 * @Author: leavesC
 * @Date: 2021/12/16 14:41
 * @Github: https://github.com/leavesC
 * @Desc:
 */
class OptimizedThreadActivity : AppCompatActivity() {

    private val btnSubmitTask by lazy {
        findViewById<Button>(R.id.btnSubmitTask)
    }

    private val tvLog by lazy {
        findViewById<TextView>(R.id.tvLog)
    }

    private val newFixedThreadPool = Executors.newFixedThreadPool(1)

    private val newSingleThreadExecutor = Executors.newSingleThreadExecutor { r ->
        Thread(
            r,
            "业志陈"
        )
    }

    private val newCachedThreadPool = Executors.newCachedThreadPool { r ->
        Thread(
            r,
            "字节数组"
        )
    }

    private val newSingleThreadScheduledExecutor = Executors.newSingleThreadScheduledExecutor { r ->
        Thread(
            r,
            "leavesC"
        )
    }

    private val newScheduledThreadPool = Executors.newScheduledThreadPool(1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_optimized_thread)
        btnSubmitTask.setOnClickListener(object : View.OnClickListener {
            @UncheckViewOnClick
            override fun onClick(v: View) {
                val task: (String) -> Unit = { threadType ->
                    Thread.sleep(Random.nextLong(300, 3000))
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
            }
        })
    }

}