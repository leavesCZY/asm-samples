package github.leavesczy.trace.plugins.click.compose

import com.android.build.api.instrumentation.FramesComputationMode
import com.android.build.api.instrumentation.InstrumentationScope
import com.android.build.api.variant.AndroidComponentsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.getByType

/**
 * @Author: leavesCZY
 * @Github: https://github.com/leavesCZY
 * @Desc:
 */
class ComposeClickPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.extensions.create<ComposeClickPluginParameter>(name = ComposeClickPluginParameter::class.java.simpleName)
        val androidComponents = project.extensions.getByType(AndroidComponentsExtension::class.java)
        androidComponents.onVariants { variant ->
            val pluginParameter = project.extensions.getByType<ComposeClickPluginParameter>()
            variant.instrumentation.transformClassesWith(
                ComposeClickClassVisitorFactory::class.java,
                InstrumentationScope.ALL
            ) { params ->
                params.config.set(ComposeClickConfig(pluginParameter = pluginParameter))
            }
            variant.instrumentation.setAsmFramesComputationMode(
                FramesComputationMode.COMPUTE_FRAMES_FOR_INSTRUMENTED_METHODS
            )
        }
    }

}