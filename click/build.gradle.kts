@file:Suppress("UnstableApiUsage")

android {
    namespace = "github.leavesczy.trace.click"
    kotlinOptions {
        freeCompilerArgs = listOf(
            "-opt-in=androidx.compose.ui.ExperimentalComposeUiApi",
            "-opt-in=androidx.compose.ui.text.ExperimentalTextApi",
            "-opt-in=androidx.compose.material.ExperimentalMaterialApi",
            "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
            "-opt-in=androidx.compose.animation.ExperimentalAnimationApi",
            "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi",
            "-opt-in=androidx.compose.foundation.layout.ExperimentalLayoutApi",
            "-opt-in=androidx.constraintlayout.compose.ExperimentalMotionApi"
        )
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Dependencies.Compose.compiler
    }
}

dependencies {
    implementation(Dependencies.Components.appcompat)
    implementation(Dependencies.Components.material)
    implementation(Dependencies.Components.baseRecyclerView)
    implementationCompose()
}