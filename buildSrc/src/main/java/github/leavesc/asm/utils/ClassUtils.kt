package github.leavesc.asm.utils

import java.io.File

/**
 * @Author: leavesC
 * @Date: 2021/12/4 17:40
 * @Desc:
 */
object ClassUtils {

    private fun isAndroidGeneratedClass(className: String): Boolean {
        return className.contains("R$") ||
                className.contains("R2$") ||
                className.contains("R.class") ||
                className.contains("R2.class") ||
                className == "BuildConfig.class"
    }

    fun isLegalJar(file: File): Boolean {
        return file.isFile &&
                file.length() > 0L &&
                file.name != "R.jar" &&
                file.name.endsWith(".jar")
    }

    fun isLegalClass(file: File): Boolean {
        return file.isFile && isLegalClass(file.name)
    }

    fun isLegalClass(fileName: String): Boolean {
        return fileName.endsWith(".class") && !isAndroidGeneratedClass(fileName)
    }

}