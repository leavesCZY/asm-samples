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
            id = "github.leavesczy.trace.click.view"
            implementationClass = "github.leavesczy.trace.plugins.click.view.ViewClickPlugin"
        }
        create("ComposeClickPlugin") {
            id = "github.leavesczy.trace.click.compose"
            implementationClass = "github.leavesczy.trace.plugins.click.compose.ComposeClickPlugin"
        }
        create("LegalBitmapPlugin") {
            id = "github.leavesczy.trace.bitmap"
            implementationClass = "github.leavesczy.trace.plugins.bitmap.LegalBitmapPlugin"
        }
        create("PrivacySentryPlugin") {
            id = "github.leavesczy.trace.privacy"
            implementationClass = "github.leavesczy.trace.plugins.privacy.PrivacySentryPlugin"
        }
        create("OptimizedThreadPlugin") {
            id = "github.leavesczy.trace.thread"
            implementationClass = "github.leavesczy.trace.plugins.thread.OptimizedThreadPlugin"
        }
    }
}

dependencies {
    val agpVersion = "8.1.3"
    val kotlinVersion = "1.9.20"
    implementation("com.android.tools.build:gradle:${agpVersion}")
    implementation("com.android.tools.build:gradle-api:${agpVersion}")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${kotlinVersion}")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin-api:${kotlinVersion}")
    implementation("org.ow2.asm:asm-commons:9.6")
    implementation("commons-io:commons-io:2.15.0")
}