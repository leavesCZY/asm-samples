extensions.configure<github.leavesczy.asm.plugins.privacy.PrivacySentryGradleConfig>("PrivacySentry") {
    methodOwner = "github.leavesczy.asm.privacy.privacy.PrivacySentryRecord"
    methodName = "writeToFile"
}

android {
    namespace = "github.leavesczy.asm"
}

dependencies {
    implementation(Dependencies.Components.appcompat)
    implementation(Dependencies.Components.material)
    implementation(project(":bitmap"))
    implementation(project(":click"))
    implementation(project(":privacy"))
    implementation(project(":thread"))
}