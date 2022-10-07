plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
}

apply {
    plugin<github.leavesczy.asm.plugins.doubleClick.DoubleClickPlugin>()
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
        applicationId = "github.leavesczy.asm_transform"
        minSdk = 23
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    implementation("androidx.appcompat:appcompat:1.5.1")
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("com.google.android.material:material:1.6.1")
    implementation("io.github.cymchad:BaseRecyclerViewAdapterHelper:3.0.9")
    implementation("commons-io:commons-io:2.6")
    implementation(project(":doubleClick"))
}