package github.leavesczy.asm.plugins.privacySentry

/**
 * @Author: leavesCZY
 * @Date: 2021/12/21 22:59
 * @Desc:
 * @Github：https://github.com/leavesCZY
 */
data class PrivacySentryConfig(
    val fieldHookPointList: List<PrivacySentryHookPoint> = filedHookPoints,
    val methodHookPointList: List<PrivacySentryHookPoint> = methodHookPoints
) {

    companion object {
        var runtimeRecord: PrivacySentryRuntimeRecord? = null
    }

}

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

data class PrivacySentryRuntimeRecord(
    val methodOwner: String,
    val methodName: String,
    val methodDesc: String
)

open class PrivacySentryGradleConfig {
    var methodOwner = ""
    var methodName = ""
    private val methodDesc = "(Ljava/lang/String;)V"

    fun transform() {
        if (methodOwner.isBlank() || methodName.isBlank()) {
            PrivacySentryConfig.runtimeRecord = null
        } else {
            PrivacySentryConfig.runtimeRecord = PrivacySentryRuntimeRecord(
                methodOwner = methodOwner.replace('.', '/'),
                methodName = methodName,
                methodDesc = methodDesc
            )
        }
    }

}