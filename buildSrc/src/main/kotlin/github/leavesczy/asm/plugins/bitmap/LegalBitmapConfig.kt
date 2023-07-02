package github.leavesczy.asm.plugins.bitmap

import java.io.Serializable

/**
 * @Author: leavesCZY
 * @Desc:
 */
data class LegalBitmapConfig(
    private val monitorImageViewClass: String = "github.leavesczy.asm.bitmap.MonitorImageView",
) : Serializable {

    val formatMonitorImageViewClass: String
        get() = monitorImageViewClass.replace(".", "/")

}