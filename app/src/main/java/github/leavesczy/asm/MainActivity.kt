package github.leavesczy.asm

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import github.leavesczy.asm.bitmap.LegalBitmapActivity
import github.leavesczy.asm.click.ComposeDoubleClickCheckActivity
import github.leavesczy.asm.click.ViewDoubleClickCheckActivity
import github.leavesczy.asm.privacy.privacy.PrivacySentryActivity
import github.leavesczy.asm.thread.OptimizedThreadActivity

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
        findViewById<View>(R.id.btnViewDoubleClickCheck).setOnClickListener {
            startActivity<ViewDoubleClickCheckActivity>()
        }
        findViewById<View>(R.id.btnComposeDoubleClickCheck).setOnClickListener {
            startActivity<ComposeDoubleClickCheckActivity>()
        }
        findViewById<View>(R.id.btnOptimizedThread).setOnClickListener {
            startActivity<OptimizedThreadActivity>()
        }
        findViewById<View>(R.id.btnPrivacySentry).setOnClickListener {
            startActivity<PrivacySentryActivity>()
        }
        findViewById<View>(R.id.btnLegalBitmap).setOnClickListener {
            startActivity<LegalBitmapActivity>()
        }
    }

    private inline fun <reified T : Activity> startActivity() {
        val intent = Intent(this, T::class.java)
        startActivity(intent)
    }

}