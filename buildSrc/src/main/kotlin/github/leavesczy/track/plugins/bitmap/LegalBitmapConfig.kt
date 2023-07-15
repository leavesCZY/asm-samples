package github.leavesczy.track.plugins.bitmap

import java.io.Serializable

/**
 * @Author: leavesCZY
 * @Github: https://github.com/leavesCZY
 * @Desc:
 */
internal data class LegalBitmapConfig(
    val imageViewClass: String
) : Serializable {

    companion object {

        operator fun invoke(pluginParameter: LegalBitmapPluginParameter): LegalBitmapConfig {
            return LegalBitmapConfig(
                imageViewClass = pluginParameter.imageViewClass.replace(
                    ".",
                    "/"
                )
            )
        }

    }

}

open class LegalBitmapPluginParameter {
    var imageViewClass = ""
}