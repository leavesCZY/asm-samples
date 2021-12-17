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

    private val fixedThreadPool = Executors.newFixedThreadPool(10)

    private val scheduledThreadPool = Executors.newScheduledThreadPool(10)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_optimized_thread)
        btnSubmitTask.setOnClickListener(object : View.OnClickListener {
            @UncheckViewOnClick
            override fun onClick(v: View) {
                fixedThreadPool.execute {
                    Thread.sleep(Random.nextLong(300, 3000))
                    val threadName = Thread.currentThread().name
                    runOnUiThread {
                        tvLog.append("\nfixedThreadPool: \n$threadName")
                    }
                }
                scheduledThreadPool.execute {
                    Thread.sleep(Random.nextLong(300, 3000))
                    val threadName = Thread.currentThread().name
                    runOnUiThread {
                        tvLog.append("\nscheduledThreadPool: \n$threadName")
                    }
                }
            }
        })
    }

}