import github.leavesczy.asm.AsmPlugin

subprojects {
    apply {
        plugin<AsmPlugin>()
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}