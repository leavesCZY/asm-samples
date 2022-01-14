package github.leavesczy.asm.plugins.double_click

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

/**
 * @Author: leavesCZY
 * @Date: 2021/12/2 16:02
 * @Desc:
 */
class DoubleClickPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val config = DoubleClickConfig()
        val appExtension: AppExtension = project.extensions.getByType()
        appExtension.registerTransform(DoubleClickTransform(config))
    }

}