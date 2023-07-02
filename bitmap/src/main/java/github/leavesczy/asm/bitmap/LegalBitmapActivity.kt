package github.leavesczy.asm.bitmap

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * @Author: leavesCZY
 * @Desc:
 */
class LegalBitmapActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_legal_bitmap)
        title = "大图检测"
    }

}