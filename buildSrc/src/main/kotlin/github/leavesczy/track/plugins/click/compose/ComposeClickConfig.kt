package github.leavesczy.track.plugins.click.compose

import java.io.Serializable

/**
 * @Author: leavesCZY
 * @Github: https://github.com/leavesCZY
 * @Desc:
 */
internal data class ComposeClickConfig(
    val onClickClass: String,
    val whiteListTag: String
) : Serializable {

    companion object {

        operator fun invoke(pluginParameter: ComposeClickPluginParameter): ComposeClickConfig {
            return ComposeClickConfig(
                onClickClass = pluginParameter.onClickClass.replace(".", "/"),
                whiteListTag = pluginParameter.whiteListTag
            )
        }

    }

}

open class ComposeClickPluginParameter {
    var onClickClass = ""
    var whiteListTag = ""
}