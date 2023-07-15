package github.leavesczy.track.plugins.privacy

import java.io.Serializable

/**
 * @Author: leavesCZY
 * @Github: https://github.com/leavesCZY
 * @Desc:
 */
internal class PrivacySentryConfig(
    val fieldHookPointList: List<PrivacySentryHookPoint>,
    val methodHookPointList: List<PrivacySentryHookPoint>,
    val runtimeRecord: PrivacySentryRuntimeRecord
) : Serializable {

    companion object {

        operator fun invoke(pluginParameter: PrivacySentryPluginParameter): PrivacySentryConfig {
            return PrivacySentryConfig(
                fieldHookPointList = listOf(
                    PrivacySentryHookPoint(
                        owner = "android/os/Build",
                        name = "BRAND",
                        desc = "Ljava/lang/String;"
                    )
                ),
                methodHookPointList = listOf(
                    PrivacySentryHookPoint(
                        owner = "android/telephony/TelephonyManager",
                        name = "getDeviceId",
                        desc = "()Ljava/lang/String;"
                    ),
                    PrivacySentryHookPoint(
                        owner = "android/provider/Settings\$Secure",
                        name = "getString",
                        desc = "(Landroid/content/ContentResolver;Ljava/lang/String;)Ljava/lang/String;"
                    )
                ),
                runtimeRecord = PrivacySentryRuntimeRecord(
                    methodOwner = pluginParameter.methodOwner.replace('.', '/'),
                    methodName = pluginParameter.methodName,
                    methodDesc = "(Ljava/lang/String;)V"
                )
            )
        }

    }

}

internal class PrivacySentryHookPoint(
    val owner: String,
    val name: String,
    val desc: String
) : Serializable

internal class PrivacySentryRuntimeRecord(
    val methodOwner: String,
    val methodName: String,
    val methodDesc: String
) : Serializable

open class PrivacySentryPluginParameter {
    var methodOwner = ""
    var methodName = ""
}