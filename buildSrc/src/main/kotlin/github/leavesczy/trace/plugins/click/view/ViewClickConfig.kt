package github.leavesczy.trace.plugins.click.view

import java.io.Serializable

/**
 * @Author: leavesCZY
 * @Github: https://github.com/leavesCZY
 * @Desc:
 */
internal data class ViewClickConfig(
    val clickCheckClass: String,
    val onClickMethodName: String,
    val checkViewOnClickAnnotation: String,
    val uncheckViewOnClickAnnotation: String,
    val hookPointList: List<ViewClickHookPoint>,
    val include: List<String>
) : Serializable {

    val onClickMethodDesc = "(Landroid/view/View;)Z"

    companion object {

        operator fun invoke(pluginParameter: ViewClickPluginParameter): ViewClickConfig {
            return ViewClickConfig(
                clickCheckClass = pluginParameter.clickCheckClass.replace(".", "/"),
                onClickMethodName = pluginParameter.onClickMethodName,
                checkViewOnClickAnnotation = formatAnnotationDesc(desc = pluginParameter.checkViewOnClickAnnotation),
                uncheckViewOnClickAnnotation = formatAnnotationDesc(desc = pluginParameter.uncheckViewOnClickAnnotation),
                include = pluginParameter.include,
                hookPointList = listOf(
                    ViewClickHookPoint(
                        interfaceName = "android/view/View\$OnClickListener",
                        methodName = "onClick",
                        nameWithDesc = "onClick(Landroid/view/View;)V"
                    ),
                    ViewClickHookPoint(
                        interfaceName = "com/chad/library/adapter/base/listener/OnItemClickListener",
                        methodName = "onItemClick",
                        nameWithDesc = "onItemClick(Lcom/chad/library/adapter/base/BaseQuickAdapter;Landroid/view/View;I)V"
                    ),
                    ViewClickHookPoint(
                        interfaceName = "com/chad/library/adapter/base/listener/OnItemChildClickListener",
                        methodName = "onItemChildClick",
                        nameWithDesc = "onItemChildClick(Lcom/chad/library/adapter/base/BaseQuickAdapter;Landroid/view/View;I)V",
                    )
                )
            )
        }

        private fun formatAnnotationDesc(desc: String): String {
            return "L" + desc.replace(
                ".",
                "/"
            ) + ";"
        }

    }

}

internal data class ViewClickHookPoint(
    val interfaceName: String,
    val methodName: String,
    val nameWithDesc: String,
) : Serializable {

    val interfaceSignSuffix = "L$interfaceName;"

}

open class ViewClickPluginParameter {
    var clickCheckClass = ""
    var onClickMethodName = ""
    var checkViewOnClickAnnotation = ""
    var uncheckViewOnClickAnnotation = ""
    var include = listOf<String>()
}