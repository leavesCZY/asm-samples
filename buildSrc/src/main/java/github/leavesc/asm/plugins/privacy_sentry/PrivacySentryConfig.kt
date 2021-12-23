package github.leavesc.asm.plugins.privacy_sentry

/**
 * @Author: leavesC
 * @Date: 2021/12/21 22:59
 * @Desc:
 * @Github：https://github.com/leavesC
 */
data class PrivacySentryConfig(
    val fieldHookPointList: List<PrivacySentryHookPoint> = filedHookPoints,
    val methodHookPointList: List<PrivacySentryHookPoint> = methodHookPoints
)

private val methodHookPoints = listOf(
    PrivacySentryHookPoint(
        owner = "android/telephony/TelephonyManager",
        name = "getDeviceId",
        desc = "()Ljava/lang/String;"
    ),
)

private val filedHookPoints = listOf(
    PrivacySentryHookPoint(
        owner = "android/os/Build",
        name = "BRAND",
        desc = "Ljava/lang/String;"
    ),
)

data class PrivacySentryHookPoint(
    val owner: String,
    val name: String,
    val desc: String
)