package github.leavesczy.asm.plugins.click.compose

import java.io.Serializable

/**
 * @Author: leavesCZY
 * @Date: 2021/12/4 16:04
 * @Desc:
 */
data class ComposeDoubleClickConfig(
    private val onOnClickWrapClass: String,
    val whiteListTag: String
) :
    Serializable {

    val formatOnClickWrapClass: String
        get() = onOnClickWrapClass.replace(".", "/")

}