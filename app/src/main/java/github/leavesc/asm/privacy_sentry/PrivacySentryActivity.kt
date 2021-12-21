package github.leavesc.asm.privacy_sentry

import android.app.Service
import android.content.Context
import android.os.Bundle
import android.telephony.TelephonyManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import github.leavesc.asm.R


/**
 * @Author: leavesC
 * @Date: 2021/12/21 23:02
 * @Desc:
 * @Github：https://github.com/leavesC
 */
class PrivacySentryActivity : AppCompatActivity() {

    private val btnGetDeviceId by lazy {
        findViewById<Button>(R.id.btnGetDeviceId)
    }

    private val tvLog by lazy {
        findViewById<TextView>(R.id.tvLog)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_sentry)
        btnGetDeviceId.setOnClickListener {
            tvLog.text = "deviceId: " + getDeviceId(this)
        }
    }

    private fun getDeviceId(context: Context): String {
        return try {
            val telephonyManager =
                context.getSystemService(Service.TELEPHONY_SERVICE) as TelephonyManager
            telephonyManager.deviceId
        } catch (e: Throwable) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            e.printStackTrace()
            ""
        }
    }

}