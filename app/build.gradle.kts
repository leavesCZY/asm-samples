android {
    namespace = "github.leavesczy.trace"
}

plugins {
    id("github.leavesczy.trace.click.view")
    id("github.leavesczy.trace.click.compose")
    id("github.leavesczy.trace.bitmap")
    id("github.leavesczy.trace.privacy")
    id("github.leavesczy.trace.thread")
}

extensions.configure<github.leavesczy.trace.plugins.click.view.ViewClickPluginParameter> {
    clickCheckClass = "github.leavesczy.trace.click.view.ViewClickMonitor"
    onClickMethodName = "onClick"
    checkViewOnClickAnnotation = "github.leavesczy.trace.click.view.CheckViewOnClick"
    uncheckViewOnClickAnnotation = "github.leavesczy.trace.click.view.UncheckViewOnClick"
    include = listOf("github.leavesczy.trace.click")
}

extensions.configure<github.leavesczy.trace.plugins.click.compose.ComposeClickPluginParameter> {
    onClickClass = "github.leavesczy.trace.click.compose.ComposeOnClick"
    whiteListTag = "noCheck"
}

extensions.configure<github.leavesczy.trace.plugins.bitmap.LegalBitmapPluginParameter> {
    imageViewClass = "github.leavesczy.trace.bitmap.MonitorImageView"
}

extensions.configure<github.leavesczy.trace.plugins.privacy.PrivacySentryPluginParameter> {
    methodOwner = "github.leavesczy.trace.privacy.PrivacySentryRecord"
    methodName = "writeToFile"
}

extensions.configure<github.leavesczy.trace.plugins.thread.OptimizedThreadPluginParameter> {
    optimizedThreadClass = "github.leavesczy.trace.thread.OptimizedThread"
    optimizedExecutorsClass = "github.leavesczy.trace.thread.OptimizedExecutors"
}

dependencies {
    implementation(Dependencies.Components.appcompat)
    implementation(Dependencies.Components.material)
    implementation(project(":bitmap"))
    implementation(project(":click"))
    implementation(project(":privacy"))
    implementation(project(":thread"))
}