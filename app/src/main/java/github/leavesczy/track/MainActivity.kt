package github.leavesczy.track

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import github.leavesczy.track.bitmap.LegalBitmapActivity
import github.leavesczy.track.click.ComposeClickCheckActivity
import github.leavesczy.track.click.ViewClickCheckActivity
import github.leavesczy.track.privacy.PrivacySentryActivity
import github.leavesczy.track.thread.OptimizedThreadActivity

/**
 * @Author: leavesCZY
 * @Github: https://github.com/leavesCZY
 * @Desc:
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.btnViewClick).setOnClickListener {
            startActivity<ViewClickCheckActivity>()
        }
        findViewById<View>(R.id.btnComposeClick).setOnClickListener {
            startActivity<ComposeClickCheckActivity>()
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