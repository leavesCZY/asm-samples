package github.leavesc.asm.plugins.privacy_sentry

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

/**
 * @Author: leavesC
 * @Date: 2021/12/21 22:57
 * @Desc:
 * @Github：https://github.com/leavesC
 */
class PrivacySentryPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        val appExtension: AppExtension = target.extensions.getByType()
        appExtension.registerTransform(
            PrivacySentryTransform(
                config = PrivacySentryConfig()
            )
        )
    }

}