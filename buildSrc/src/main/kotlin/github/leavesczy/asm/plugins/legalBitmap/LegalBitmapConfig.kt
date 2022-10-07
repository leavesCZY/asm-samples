package github.leavesczy.asm.plugins.legalBitmap

import java.io.Serializable

/**
 * @Author: leavesCZY
 * @Desc:
 */
data class LegalBitmapConfig(
    private val monitorImageViewClass: String = "github.leavesczy.asm.legalBitmap.MonitorImageView",
) : Serializable {

    val formatMonitorImageViewClass: String
        get() = monitorImageViewClass.replace(".", "/")

}