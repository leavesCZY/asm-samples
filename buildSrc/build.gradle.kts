repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
}

plugins {
    `kotlin-dsl`
}

sourceSets {
    main {
        java {
            srcDir("src/main/kotlin")
        }
    }
}

dependencies {
    implementation("com.android.tools.build:gradle:8.0.0-alpha02")
    compileOnly("org.ow2.asm:asm-commons:9.3")
    compileOnly("commons-io:commons-io:2.6")
}