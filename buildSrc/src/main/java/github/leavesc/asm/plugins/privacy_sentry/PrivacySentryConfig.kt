package github.leavesc.asm.plugins.privacy_sentry

/**
 * @Author: leavesC
 * @Date: 2021/12/21 22:59
 * @Desc:
 * @Github：https://github.com/leavesC
 */
data class PrivacySentryConfig(
    val fieldHookPointList: List<PrivacySentryFieldHookPoint> = filedHookPoints,
    val methodHookPointList: List<PrivacySentryMethodHookPoint> = methodHookPoints
)

private val methodHookPoints = listOf(
    PrivacySentryMethodHookPoint(
        owner = "android/telephony/TelephonyManager",
        name = "getDeviceId",
        desc = "()Ljava/lang/String;"
    ),
    PrivacySentryMethodHookPoint(
        owner = "android.os.Build",
        name = "getDeviceId",
        desc = "()Ljava/lang/String;"
    ),
)

private val filedHookPoints = listOf(
    PrivacySentryFieldHookPoint(
        owner = "android.os.Build",
        name = "BRAND",
        desc = "Ljava/lang/String;"
    ),
)

data class PrivacySentryMethodHookPoint(
    val owner: String,
    val name: String,
    val desc: String
)

data class PrivacySentryFieldHookPoint(
    val owner: String,
    val name: String,
    val desc: String
)