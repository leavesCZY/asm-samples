package github.leavesczy.asm.plugins.legalBitmap

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

/**
 * @Author: leavesCZY
 * @Desc:
 */
class LegalBitmapPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val config = LegalBitmapConfig()
        val appExtension: AppExtension = project.extensions.getByType()
        appExtension.registerTransform(LegalBitmapTransform(config))
    }

}