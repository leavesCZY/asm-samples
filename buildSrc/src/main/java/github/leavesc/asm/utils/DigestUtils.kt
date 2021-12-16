package github.leavesc.asm.utils

import org.apache.commons.codec.binary.Hex
import java.io.File

/**
 * @Author: leavesC
 * @Date: 2021/12/8 15:45
 * @Desc:
 */
object DigestUtils {

    fun generateJarFileName(jarFile: File): String {
        return getMd5ByFilePath(jarFile) + "_" + jarFile.name
    }

    fun generateClassFileName(classFile: File): String {
        return getMd5ByFilePath(classFile) + "_" + classFile.name
    }

    private fun getMd5ByFilePath(file: File): String {
        return Hex.encodeHexString(file.absolutePath.toByteArray()).substring(0, 8)
    }

}