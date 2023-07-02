package github.leavesczy.asm.plugins.privacy

import com.android.build.api.instrumentation.FramesComputationMode
import com.android.build.api.instrumentation.InstrumentationScope
import com.android.build.api.variant.AndroidComponentsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.getByType

/**
 * @Author: leavesCZY
 * @Date: 2021/12/21 22:57
 * @Desc:
 * @Github：https://github.com/leavesCZY
 */
class PrivacySentryPlugin : Plugin<Project> {

    companion object {

        private const val EXT_NAME = "PrivacySentry"

    }

    override fun apply(project: Project) {
        project.extensions.create<PrivacySentryGradleConfig>(EXT_NAME)
        project.afterEvaluate {
            val config = project.extensions.getByType<PrivacySentryGradleConfig>()
            config.transform()
        }
        val androidComponents = project.extensions.getByType(AndroidComponentsExtension::class.java)
        androidComponents.onVariants { variant ->
            variant.instrumentation.transformClassesWith(
                PrivacySentryClassVisitorFactory::class.java,
                InstrumentationScope.ALL
            ) { params ->
                params.config.set(PrivacySentryConfig())
            }
            variant.instrumentation.setAsmFramesComputationMode(
                FramesComputationMode.COMPUTE_FRAMES_FOR_INSTRUMENTED_METHODS
            )
        }
    }

}