package github.leavesczy.track.configure

import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import github.leavesczy.track.TrackBuildConfig
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.TimeZone

/**
 * @Author: leavesCZY
 * @Github: https://github.com/leavesCZY
 * @Desc:
 */
internal fun BaseAppModuleExtension.appModule(project: Project) {
    compileSdk = TrackBuildConfig.compileSdk
    buildToolsVersion = TrackBuildConfig.buildToolsVersion
    defaultConfig {
        applicationId = TrackBuildConfig.applicationId
        minSdk = TrackBuildConfig.minSdk
        targetSdk = TrackBuildConfig.targetSdk
        versionCode = TrackBuildConfig.versionCode
        versionName = TrackBuildConfig.versionName
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    signingConfigs {
        create("release") {
            storeFile =
                File(project.rootDir.absolutePath + File.separator + "doc" + File.separator + "key.jks")
            keyAlias = TrackBuildConfig.keyAlias
            keyPassword = TrackBuildConfig.keyPassword
            storePassword = TrackBuildConfig.storePassword
            enableV1Signing = true
            enableV2Signing = true
        }
    }
    buildTypes {
        debug {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
            isShrinkResources = false
            isDebuggable = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    applicationVariants.all {
        val variant = this
        outputs.all {
            if (this is com.android.build.gradle.internal.api.ApkVariantOutputImpl) {
                val simpleDateFormat = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss")
                simpleDateFormat.timeZone = TimeZone.getTimeZone("Asia/Shanghai")
                val time = simpleDateFormat.format(Calendar.getInstance().time)
                this.outputFileName =
                    "asm-samples_${variant.name}_versionCode_${variant.versionCode}_versionName_${variant.versionName}_${time}.apk"
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    ((this as ExtensionAware).extensions.getByName("kotlinOptions") as KotlinJvmOptions).apply {
        jvmTarget = "17"
    }
    packaging {
        jniLibs {
            excludes += listOf("META-INF/{AL2.0,LGPL2.1}")
        }
        resources {
            excludes += listOf(
                "**/*.md",
                "**/*.version",
                "**/*.properties",
                "**/**/*.properties",
                "META-INF/{AL2.0,LGPL2.1}",
                "META-INF/CHANGES",
                "DebugProbesKt.bin",
                "kotlin-tooling-metadata.json"
            )
        }
    }
    lint {
        checkDependencies = true
    }
}