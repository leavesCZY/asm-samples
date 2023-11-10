import org.gradle.kotlin.dsl.DependencyHandlerScope

/**
 * @Author: leavesCZY
 * @Github: https://github.com/leavesCZY
 * @Desc:
 */
private fun DependencyHandlerScope.implementationExtend(library: Any) {
    this.add("implementation", library)
}

private fun DependencyHandlerScope.debugImplementationExtend(library: Any) {
    this.add("debugImplementation", library)
}

private fun DependencyHandlerScope.testImplementationExtend(library: Any) {
    this.add("testImplementation", library)
}

private fun DependencyHandlerScope.androidTestImplementationExtend(library: Any) {
    this.add("androidTestImplementation", library)
}

internal fun DependencyHandlerScope.implementationTest() {
    testImplementationExtend(Dependencies.Test.junit)
    androidTestImplementationExtend(Dependencies.Test.testExt)
    androidTestImplementationExtend(Dependencies.Test.espresso)
}

fun DependencyHandlerScope.implementationCompose() {
    val platform = platform(Dependencies.Compose.bom)
    implementationExtend(platform)
    androidTestImplementationExtend(platform)
    implementationExtend(Dependencies.Compose.ui)
    implementationExtend(Dependencies.Compose.material3)
    implementationExtend(Dependencies.Compose.activity)
}