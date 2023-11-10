package github.leavesczy.trace.bitmap

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * @Author: leavesCZY
 * @Github: https://github.com/leavesCZY
 * @Desc:
 */
class LegalBitmapActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_legal_bitmap)
        title = "大图检测"
    }

}