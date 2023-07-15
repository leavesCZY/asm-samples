package github.leavesczy.track.privacy

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager

/**
 * @Author: leavesCZY
 * @Github: https://github.com/leavesCZY
 * @Desc:
 */
internal object DeviceUtils {

    @SuppressLint("MissingPermission", "HardwareIds")
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

    @SuppressLint("HardwareIds")
    fun getAndroidId(context: Context): String {
        return try {
            Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID) ?: ""
        } catch (throwable: Throwable) {
            ""
        }
    }

    fun getBrand(): String {
        return Build.BRAND
    }

}