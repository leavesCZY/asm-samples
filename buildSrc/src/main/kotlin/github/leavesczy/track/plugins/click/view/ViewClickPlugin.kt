package github.leavesczy.track.plugins.click.view

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
class ViewClickPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.extensions.create<ViewClickPluginParameter>(name = ViewClickPluginParameter::class.java.simpleName)
        val androidComponents = project.extensions.getByType(AndroidComponentsExtension::class.java)
        androidComponents.onVariants { variant ->
            val pluginParameter = project.extensions.getByType<ViewClickPluginParameter>()
            variant.instrumentation.transformClassesWith(
                ViewClickClassVisitorFactory::class.java,
                InstrumentationScope.ALL
            ) { params ->
                params.config.set(ViewClickConfig(pluginParameter = pluginParameter))
            }
            variant.instrumentation.setAsmFramesComputationMode(
                FramesComputationMode.COMPUTE_FRAMES_FOR_INSTRUMENTED_METHODS
            )
        }
    }

}