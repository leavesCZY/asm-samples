@file:Suppress("UnstableApiUsage")

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

apply {
    plugin<github.leavesczy.asm.plugins.doubleClick.view.ViewDoubleClickPlugin>()
    plugin<github.leavesczy.asm.plugins.doubleClick.compose.ComposeDoubleClickPlugin>()
    plugin<github.leavesczy.asm.plugins.legalBitmap.LegalBitmapPlugin>()
    plugin<github.leavesczy.asm.plugins.optimizedThread.OptimizedThreadPlugin>()
    plugin<github.leavesczy.asm.plugins.privacySentry.PrivacySentryPlugin>()
}

extensions.configure<github.leavesczy.asm.plugins.privacySentry.PrivacySentryGradleConfig>("PrivacySentry") {
    methodOwner = "github.leavesczy.asm.privacySentry.PrivacySentryRecord"
    methodName = "writeToFile"
}

android {
    namespace = "github.leavesczy.asm"
    compileSdk = 33
    defaultConfig {
        applicationId = "github.leavesczy.asm"
        minSdk = 23
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    signingConfigs {
        create("release") {
            storeFile = File(project.rootDir.absolutePath + File.separator + "key.jks")
            keyAlias = "leavesCZY"
            storePassword = "123456"
            keyPassword = "123456"
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs = freeCompilerArgs.toMutableList().apply {
            addAll(
                listOf(
                    "-Xopt-in=androidx.compose.ui.ExperimentalComposeUiApi",
                    "-Xopt-in=androidx.compose.ui.text.ExperimentalTextApi",
                    "-Xopt-in=androidx.compose.animation.ExperimentalAnimationApi",
                    "-Xopt-in=androidx.compose.foundation.ExperimentalFoundationApi",
                    "-Xopt-in=androidx.compose.material3.ExperimentalMaterial3Api",
                )
            )
        }
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.7"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("io.github.cymchad:BaseRecyclerViewAdapterHelper:3.0.9")
    implementation("commons-io:commons-io:2.11.0")
    val composeBom = platform("androidx.compose:compose-bom:2023.05.01")
    implementation(composeBom)
    androidTestImplementation(composeBom)
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.activity:activity-compose:1.7.1")
}