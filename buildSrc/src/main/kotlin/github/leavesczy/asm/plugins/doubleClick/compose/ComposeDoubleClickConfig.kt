package github.leavesczy.asm.plugins.doubleClick.compose

import java.io.Serializable

/**
 * @Author: leavesCZY
 * @Date: 2021/12/4 16:04
 * @Desc:
 */
data class ComposeDoubleClickConfig(private val clickableWrapClass: String) : Serializable {

    val formatClickableWrapClass: String
        get() = clickableWrapClass.replace(".", "/")

}