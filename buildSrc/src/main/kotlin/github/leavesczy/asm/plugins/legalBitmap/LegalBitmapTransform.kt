package github.leavesczy.asm.plugins.legalBitmap

import github.leavesczy.asm.base.BaseTransform
import github.leavesczy.asm.utils.Log
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes

/**
 * @Author: leavesCZY
 * @Desc:
 */
class LegalBitmapTransform(private val config: LegalBitmapConfig) : BaseTransform() {

    private companion object {

        private const val ImageViewClass = "android/widget/ImageView"

    }

    override fun modifyClass(byteArray: ByteArray): ByteArray {
        val classReader = ClassReader(byteArray)
        val className = classReader.className
        val superName = classReader.superName
        Log.log("className: $className superName: $superName")
        return if (className != config.formatMonitorImageViewClass && superName == ImageViewClass) {
            val classWriter = ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
            val classVisitor = object : ClassVisitor(Opcodes.ASM6, classWriter) {
                override fun visit(
                    version: Int,
                    access: Int,
                    name: String?,
                    signature: String?,
                    superName: String?,
                    interfaces: Array<out String>?
                ) {
                    super.visit(
                        version,
                        access,
                        name,
                        signature,
                        config.formatMonitorImageViewClass,
                        interfaces
                    )
                }
            }
            classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)
            classWriter.toByteArray()
        } else {
            byteArray
        }
    }

}