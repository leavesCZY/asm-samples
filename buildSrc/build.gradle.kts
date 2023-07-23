repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
}

plugins {
    `kotlin-dsl`
    id("java-gradle-plugin")
}

gradlePlugin {
    plugins {
        create("ViewClickPlugin") {
            id = "github.leavesczy.track.click.view"
            implementationClass = "github.leavesczy.track.plugins.click.view.ViewClickPlugin"
        }
        create("ComposeClickPlugin") {
            id = "github.leavesczy.track.click.compose"
            implementationClass = "github.leavesczy.track.plugins.click.compose.ComposeClickPlugin"
        }
        create("LegalBitmapPlugin") {
            id = "github.leavesczy.track.bitmap"
            implementationClass = "github.leavesczy.track.plugins.bitmap.LegalBitmapPlugin"
        }
        create("PrivacySentryPlugin") {
            id = "github.leavesczy.track.privacy"
            implementationClass = "github.leavesczy.track.plugins.privacy.PrivacySentryPlugin"
        }
        create("OptimizedThreadPlugin") {
            id = "github.leavesczy.track.thread"
            implementationClass = "github.leavesczy.track.plugins.thread.OptimizedThreadPlugin"
        }
    }
}

dependencies {
    val agpVersion = "8.0.2"
    val kotlinVersion = "1.8.22"
    implementation("com.android.tools.build:gradle:${agpVersion}")
    implementation("com.android.tools.build:gradle-api:${agpVersion}")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${kotlinVersion}")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin-api:${kotlinVersion}")
    implementation("org.ow2.asm:asm-commons:9.5")
    implementation("commons-io:commons-io:2.13.0")
}