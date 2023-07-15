package github.leavesczy.track.plugins.bitmap

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
class LegalBitmapPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.extensions.create<LegalBitmapPluginParameter>(name = LegalBitmapPluginParameter::class.java.simpleName)
        val androidComponents = project.extensions.getByType(AndroidComponentsExtension::class.java)
        androidComponents.onVariants { variant ->
            val pluginParameter = project.extensions.getByType<LegalBitmapPluginParameter>()
            variant.instrumentation.transformClassesWith(
                LegalBitmapClassVisitorFactory::class.java,
                InstrumentationScope.ALL
            ) { params ->
                params.config.set(LegalBitmapConfig(pluginParameter = pluginParameter))
            }
            variant.instrumentation.setAsmFramesComputationMode(
                FramesComputationMode.COMPUTE_FRAMES_FOR_INSTRUMENTED_METHODS
            )
        }
    }

}