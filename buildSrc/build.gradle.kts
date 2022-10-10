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
    val agpVersion = "7.2.2"
    val kotlinVersion = "1.7.20"
    implementation("com.android.tools.build:gradle:${agpVersion}")
    implementation("com.android.tools.build:gradle-api:${agpVersion}")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${kotlinVersion}")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin-api:${kotlinVersion}")
    compileOnly("org.ow2.asm:asm-commons:9.3")
    compileOnly("commons-io:commons-io:2.6")
}