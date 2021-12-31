package github.leavesc.asm.privacy_sentry

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
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

    private val btnGetDeviceBrand by lazy {
        findViewById<Button>(R.id.btnGetDeviceBrand)
    }

    private val tvLog by lazy {
        findViewById<TextView>(R.id.tvLog)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_sentry)
        showLog("可以通过 adb 命令导出运行时记录的文件到当前工程目录下：adb pull ${PrivacySentryRecord.logFile.absolutePath} ")
        btnGetDeviceId.setOnClickListener {
            showLog("deviceId: " + DeviceUtils.getDeviceId(this))
        }
        btnGetDeviceBrand.setOnClickListener {
            showLog("brand: " + DeviceUtils.getBrand())
        }
    }

    private fun showLog(log: String) {
        tvLog.append("\n" + log)
        Log.e("PrivacySentryActivity", log)
    }

}