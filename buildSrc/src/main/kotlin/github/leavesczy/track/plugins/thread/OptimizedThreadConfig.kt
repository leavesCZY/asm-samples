package github.leavesczy.track.plugins.thread

import java.io.Serializable

/**
 * @Author: leavesCZY
 * @Github: https://github.com/leavesCZY
 * @Desc:
 */
internal class OptimizedThreadConfig(
    val optimizedThreadClass: String,
    val optimizedExecutorsClass: String,
    val executorsMethodNameList: List<String>
) : Serializable {

    companion object {

        operator fun invoke(pluginParameter: OptimizedThreadPluginParameter): OptimizedThreadConfig {
            return OptimizedThreadConfig(
                optimizedThreadClass = pluginParameter.optimizedThreadClass.replace(".", "/"),
                optimizedExecutorsClass = pluginParameter.optimizedExecutorsClass.replace(
                    ".",
                    "/"
                ),
                executorsMethodNameList = listOf(
                    "newFixedThreadPool",
                    "newSingleThreadExecutor",
                    "newCachedThreadPool",
                    "newSingleThreadScheduledExecutor",
                    "newScheduledThreadPool"
                )
            )
        }

    }

}

open class OptimizedThreadPluginParameter {
    var optimizedThreadClass = ""
    var optimizedExecutorsClass = ""
}