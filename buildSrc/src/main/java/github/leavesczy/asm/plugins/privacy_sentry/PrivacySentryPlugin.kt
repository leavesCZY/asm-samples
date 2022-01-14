package github.leavesczy.asm.plugins.privacy_sentry

import com.android.build.gradle.AppExtension
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
            val config = (extensions.findByName(EXT_NAME) as? PrivacySentryGradleConfig)
                ?: PrivacySentryGradleConfig()
            config.transform()
        }
        val appExtension: AppExtension = project.extensions.getByType()
        appExtension.registerTransform(
            PrivacySentryTransform(
                config = PrivacySentryConfig()
            )
        )
    }

}