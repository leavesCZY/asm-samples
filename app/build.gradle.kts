android {
    namespace = "github.leavesczy.track"
}

plugins {
    id("github.leavesczy.track.click.view")
    id("github.leavesczy.track.click.compose")
    id("github.leavesczy.track.bitmap")
    id("github.leavesczy.track.privacy")
    id("github.leavesczy.track.thread")
}

extensions.configure<github.leavesczy.track.plugins.click.view.ViewClickPluginParameter> {
    clickCheckClass = "github.leavesczy.track.click.view.ViewClickMonitor"
    onClickMethodName = "onClick"
    checkViewOnClickAnnotation = "github.leavesczy.track.click.view.CheckViewOnClick"
    uncheckViewOnClickAnnotation = "github.leavesczy.track.click.view.UncheckViewOnClick"
    include = listOf("github.leavesczy.track.click")
}

extensions.configure<github.leavesczy.track.plugins.click.compose.ComposeClickPluginParameter> {
    onClickClass = "github.leavesczy.track.click.compose.ComposeOnClick"
    whiteListTag = "noCheck"
}

extensions.configure<github.leavesczy.track.plugins.bitmap.LegalBitmapPluginParameter> {
    imageViewClass = "github.leavesczy.track.bitmap.MonitorImageView"
}

extensions.configure<github.leavesczy.track.plugins.privacy.PrivacySentryPluginParameter> {
    methodOwner = "github.leavesczy.track.privacy.PrivacySentryRecord"
    methodName = "writeToFile"
}

extensions.configure<github.leavesczy.track.plugins.thread.OptimizedThreadPluginParameter> {
    optimizedThreadClass = "github.leavesczy.track.thread.OptimizedThread"
    optimizedExecutorsClass = "github.leavesczy.track.thread.OptimizedExecutors"
}

dependencies {
    implementation(Dependencies.Components.appcompat)
    implementation(Dependencies.Components.material)
    implementation(project(":bitmap"))
    implementation(project(":click"))
    implementation(project(":privacy"))
    implementation(project(":thread"))
}