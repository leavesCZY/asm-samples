package github.leavesc.asm.plugins.privacy_sentry

/**
 * @Author: leavesC
 * @Date: 2021/12/21 22:59
 * @Desc:
 * @Github：https://github.com/leavesC
 */
data class PrivacySentryConfig(val hookPointList: List<PrivacySentryHookPoint> = hookPoints)

private val hookPoints = listOf(
    PrivacySentryHookPoint(
        methodOwner = "android/telephony/TelephonyManager",
        methodName = "getDeviceId",
        methodDesc = "()Ljava/lang/String;"
    ),
)

data class PrivacySentryHookPoint(
    val methodOwner: String,
    val methodName: String,
    val methodDesc: String
)