package github.leavesczy.asm.plugins.doubleClick.view

import java.io.Serializable

/**
 * @Author: leavesCZY
 * @Date: 2021/12/4 16:04
 * @Desc:
 */
data class ViewDoubleClickConfig(
    private val doubleCheckClass: String = "github.leavesczy.asm.doubleClick.view.ViewDoubleClickCheck",
    val doubleCheckMethodName: String = "canClick",
    private val checkViewOnClickAnnotation: String = "github.leavesczy.asm.doubleClick.CheckViewOnClick",
    private val uncheckViewOnClickAnnotation: String = "github.leavesczy.asm.doubleClick.UncheckViewOnClick",
    val hookPointList: List<ViewDoubleClickHookPoint> = extraHookPoints
) : Serializable {

    val doubleCheckMethodDescriptor = "(Landroid/view/View;)Z"

    val formatDoubleCheckClass: String
        get() = doubleCheckClass.replace(".", "/")

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
//    DoubleClickHookPoint(
//        interfaceName = "com/chad/library/adapter/base/listener/OnItemClickListener",
//        methodName = "onItemClick",
//        nameWithDesc = "onItemClick(Lcom/chad/library/adapter/base/BaseQuickAdapter;Landroid/view/View;I)V"
//    ),
//    DoubleClickHookPoint(
//        interfaceName = "com/chad/library/adapter/base/listener/OnItemChildClickListener",
//        methodName = "onItemChildClick",
//        nameWithDesc = "onItemChildClick(Lcom/chad/library/adapter/base/BaseQuickAdapter;Landroid/view/View;I)V",
//    )
)