package github.leavesczy.track.configure

import com.android.build.gradle.LibraryExtension
import github.leavesczy.track.TrackBuildConfig
import org.gradle.api.JavaVersion
import org.gradle.api.plugins.ExtensionAware
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import java.io.File

/**
 * @Author: leavesCZY
 * @Github: https://github.com/leavesCZY
 * @Desc:
 */
internal fun LibraryExtension.libraryModule() {
    compileSdk = TrackBuildConfig.compileSdk
    buildToolsVersion = TrackBuildConfig.buildToolsVersion
    defaultConfig {
        minSdk = TrackBuildConfig.minSdk
        consumerProguardFiles.add(File("consumer-rules.pro"))
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    ((this as ExtensionAware).extensions.getByName("kotlinOptions") as KotlinJvmOptions).apply {
        jvmTarget = "17"
    }
}