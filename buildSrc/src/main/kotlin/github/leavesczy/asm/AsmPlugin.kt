package github.leavesczy.asm

import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import github.leavesczy.asm.configure.appModule
import github.leavesczy.asm.configure.libraryModule
import github.leavesczy.asm.plugins.bitmap.LegalBitmapPlugin
import github.leavesczy.asm.plugins.click.compose.ComposeDoubleClickPlugin
import github.leavesczy.asm.plugins.click.view.ViewDoubleClickPlugin
import github.leavesczy.asm.plugins.privacy.PrivacySentryPlugin
import github.leavesczy.asm.plugins.thread.OptimizedThreadPlugin
import implementationCompose
import implementationTest
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.DependencyHandlerScope

class AsmPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val projectName = project.name
        if (projectName == "app") {
            project.apply {
                plugin("com.android.application")
                plugin("org.jetbrains.kotlin.android")
                plugin(ComposeDoubleClickPlugin::class.java)
                plugin(LegalBitmapPlugin::class.java)
                plugin(OptimizedThreadPlugin::class.java)
                plugin(PrivacySentryPlugin::class.java)
            }
        } else {
            project.apply {
                plugin("com.android.library")
                plugin("org.jetbrains.kotlin.android")
            }
        }
        if (projectName == "click") {
            project.apply {
                plugin(ViewDoubleClickPlugin::class.java)
            }
        }
        DependencyHandlerScope.of(project.dependencies).apply {
            implementationTest()
            implementationCompose()
        }
        when (val androidExtension = project.extensions.getByName("android")) {
            is BaseAppModuleExtension -> {
                androidExtension.appModule(project = project)
            }

            is LibraryExtension -> {
                androidExtension.libraryModule()
            }
        }
    }

}