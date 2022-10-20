package github.leavesczy.asm.plugins.doubleClick.view

import java.io.Serializable

/**
 * @Author: leavesCZY
 * @Date: 2021/12/4 16:04
 * @Desc:
 */
data class ViewDoubleClickConfig(
    private val doubleClickCheckClass: String = "github.leavesczy.asm.doubleClick.view.ViewDoubleClickCheck",
    val onClickMethodName: String = "onClick",
    private val checkViewOnClickAnnotation: String = "github.leavesczy.asm.doubleClick.view.annotation.CheckViewOnClick",
    private val uncheckViewOnClickAnnotation: String = "github.leavesczy.asm.doubleClick.view.annotation.UncheckViewOnClick",
    val hookPointList: List<ViewDoubleClickHookPoint> = extraHookPoints
) : Serializable {

    val onClickMethodDesc = "(Landroid/view/View;)Z"

    val formatDoubleClickCheckClass: String
        get() = doubleClickCheckClass.replace(".", "/")

    val formatCheckViewOnClickAnnotation: String
        get() = "L" + checkViewOnClickAnnotation.replace(".", "/") + ";"

    val formatUncheckViewOnClickAnnotation: String
        get() = "L" + uncheckViewOnClickAnnotation.replace(".", "/") + ";"

}

data class ViewDoubleClickHookPoint(
    val interfaceName: String,
    val methodName: String,
    val nameWithDesc: String,
) : Serializable {

    val interfaceSignSuffix = "L$interfaceName;"

}

private val extraHookPoints = listOf(
    ViewDoubleClickHookPoint(
        interfaceName = "android/view/View\$OnClickListener",
        methodName = "onClick",
        nameWithDesc = "onClick(Landroid/view/View;)V"
    ),
    ViewDoubleClickHookPoint(
        interfaceName = "com/chad/library/adapter/base/listener/OnItemClickListener",
        methodName = "onItemClick",
        nameWithDesc = "onItemClick(Lcom/chad/library/adapter/base/BaseQuickAdapter;Landroid/view/View;I)V"
    ),
    ViewDoubleClickHookPoint(
        interfaceName = "com/chad/library/adapter/base/listener/OnItemChildClickListener",
        methodName = "onItemChildClick",
        nameWithDesc = "onItemChildClick(Lcom/chad/library/adapter/base/BaseQuickAdapter;Landroid/view/View;I)V",
    )
)