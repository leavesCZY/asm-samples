package github.leavesczy.asm.plugins.doubleClick.compose

import com.android.build.api.instrumentation.FramesComputationMode
import com.android.build.api.instrumentation.InstrumentationScope
import com.android.build.api.variant.AndroidComponentsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * @Author: leavesCZY
 * @Date: 2021/12/2 16:02
 * @Desc:
 */
class ComposeDoubleClickPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val androidComponents = project.extensions.getByType(AndroidComponentsExtension::class.java)
        androidComponents.onVariants { variant ->
            variant.instrumentation.transformClassesWith(
                ComposeDoubleClickClassVisitorFactory::class.java,
                InstrumentationScope.ALL
            ) { params ->
                params.config.set(
                    ComposeDoubleClickConfig(
                        onOnClickWrapClass = "github.leavesczy.asm.doubleClick.compose.OnClickWrap",
                        whiteListTag = "noCheck"
                    )
                )
            }
            variant.instrumentation.setAsmFramesComputationMode(
                FramesComputationMode.COMPUTE_FRAMES_FOR_INSTRUMENTED_METHODS
            )
        }
    }

}