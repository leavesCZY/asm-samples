package github.leavesczy.asm

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import github.leavesczy.asm.double_click.ViewDoubleClickCheckActivity
import github.leavesczy.asm.optimized_thread.OptimizedThreadActivity
import github.leavesczy.asm.privacy_sentry.PrivacySentryActivity

/**
 * @Author: leavesCZY
 * @Date: 2021/12/16 14:39
 * @Github: https://github.com/leavesCZY
 * @Desc:
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<TextView>(R.id.tvViewDoubleClickCheck).setOnClickListener {
            startActivity(Intent(this, ViewDoubleClickCheckActivity::class.java))
        }
        findViewById<TextView>(R.id.tvOptimizedThread).setOnClickListener {
            startActivity(Intent(this, OptimizedThreadActivity::class.java))
        }
        findViewById<TextView>(R.id.tvPrivacySentry).setOnClickListener {
            startActivity(Intent(this, PrivacySentryActivity::class.java))
        }
    }

}