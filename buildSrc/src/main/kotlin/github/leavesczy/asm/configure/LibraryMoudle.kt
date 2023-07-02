package github.leavesczy.asm.configure

import Dependencies
import com.android.build.gradle.LibraryExtension
import github.leavesczy.asm.AsmBuildConfig
import org.gradle.api.JavaVersion
import org.gradle.api.plugins.ExtensionAware
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import java.io.File

internal fun LibraryExtension.libraryModule() {
    compileSdk = AsmBuildConfig.compileSdk
    buildToolsVersion = AsmBuildConfig.buildToolsVersion
    defaultConfig {
        minSdk = AsmBuildConfig.minSdk
        targetSdk = AsmBuildConfig.targetSdk
        consumerProguardFiles.add(File("consumer-rules.pro"))
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Dependencies.Compose.compilerVersion
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    ((this as ExtensionAware).extensions.getByName("kotlinOptions") as KotlinJvmOptions).apply {
        jvmTarget = "17"
        freeCompilerArgs = freeCompilerArgs.toMutableList().apply {
            addAll(
                listOf(
                    "-opt-in=androidx.compose.ui.ExperimentalComposeUiApi",
                    "-opt-in=androidx.compose.ui.text.ExperimentalTextApi",
                    "-opt-in=androidx.compose.material.ExperimentalMaterialApi",
                    "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
                    "-opt-in=androidx.compose.animation.ExperimentalAnimationApi",
                    "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi",
                    "-opt-in=androidx.compose.foundation.layout.ExperimentalLayoutApi",
                    "-opt-in=androidx.constraintlayout.compose.ExperimentalMotionApi"
                )
            )
        }
    }
}