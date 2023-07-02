package github.leavesczy.asm.privacy.privacy

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.os.Build
import android.telephony.TelephonyManager

/**
 * @Author: leavesCZY
 * @Date: 2021/12/22 10:47
 * @Desc:
 */
object DeviceUtils {

    @SuppressLint("MissingPermission")
    fun getDeviceId(context: Context): String {
        return try {
            val telephonyManager =
                context.getSystemService(Service.TELEPHONY_SERVICE) as? TelephonyManager
            telephonyManager?.deviceId ?: ""
        } catch (e: Throwable) {
            e.printStackTrace()
            ""
        }
    }

    fun getBrand(): String {
        return Build.BRAND
    }

}