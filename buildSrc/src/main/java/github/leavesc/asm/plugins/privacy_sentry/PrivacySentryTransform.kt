package github.leavesc.asm.plugins.privacy_sentry

import github.leavesc.asm.base.BaseTransform
import github.leavesc.asm.utils.Log
import org.apache.commons.io.FileUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.*
import java.io.File
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.*
import javax.swing.filechooser.FileSystemView

/**
 * @Author: leavesC
 * @Date: 2021/12/21 22:58
 * @Desc:
 * @Github：https://github.com/leavesC
 */
class PrivacySentryTransform(private val config: PrivacySentryConfig) : BaseTransform() {

    companion object {

        private const val printStackTraceMethodName = "printStackTrace"

        private const val printStackTraceMethodDesc = "(Ljava/lang/String;)V"

    }

    private val allLintLog = StringBuffer()

    private var runtimeRecord: PrivacySentryRuntimeRecord? = null

    override fun onTransformStart() {
        runtimeRecord = PrivacySentryConfig.runtimeRecord
    }

    override fun modifyClass(byteArray: ByteArray): ByteArray {
        val classNode = ClassNode()
        val classReader = ClassReader(byteArray)
        classReader.accept(classNode, ClassReader.EXPAND_FRAMES)
        val methods = classNode.methods
        val mRuntimeRecord = runtimeRecord
        if (!methods.isNullOrEmpty()) {
            val taskList = mutableListOf<() -> Unit>()
            val tempLintLog = StringBuilder()
            for (methodNode in methods) {
                val instructions = methodNode.instructions
                val instructionIterator = instructions?.iterator()
                if (instructionIterator != null) {
                    while (instructionIterator.hasNext()) {
                        val instruction = instructionIterator.next()
                        if (instruction.isHookPoint()) {
                            val lintLog = getLintLog(
                                classNode,
                                methodNode,
                                instruction
                            )
                            tempLintLog.append(lintLog)
                            tempLintLog.append("\n\n")

                            if (mRuntimeRecord != null) {
                                taskList.add {
                                    insertRuntimeLog(
                                        classNode,
                                        methodNode,
                                        instruction
                                    )
                                }
                            }
                            Log.log(
                                "PrivacySentryTransform $lintLog"
                            )
                        }
                    }
                }
            }
            if (tempLintLog.isNotBlank()) {
                allLintLog.append(tempLintLog)
            }
            if (taskList.isNotEmpty() && mRuntimeRecord != null) {
                taskList.forEach {
                    it.invoke()
                }
                val classWriter = ClassWriter(ClassWriter.COMPUTE_MAXS)
                classNode.accept(classWriter)
                generatePrintStackTraceMethod(classWriter, mRuntimeRecord)
                return classWriter.toByteArray()
            }
        }
        return byteArray
    }

    private fun AbstractInsnNode.isHookPoint(): Boolean {
        when (this) {
            is MethodInsnNode -> {
                val owner = this.owner
                val desc = this.desc
                val name = this.name
                val find = config.methodHookPointList.find {
                    it.owner == owner && it.desc == desc
                            && it.name == name
                }
                return find != null
            }
            is FieldInsnNode -> {
                val owner = this.owner
                val desc = this.desc
                val name = this.name
                val find = config.fieldHookPointList.find {
                    it.owner == owner && it.desc == desc && it.name == name
                }
                return find != null
            }
        }
        return false
    }

    private fun getLintLog(
        classNode: ClassNode,
        methodNode: MethodNode,
        hokeInstruction: AbstractInsnNode
    ): StringBuilder {
        val classPath = classNode.name
        val methodName = methodNode.name
        val methodDesc = methodNode.desc
        val owner: String
        val desc: String
        val name: String
        when (hokeInstruction) {
            is MethodInsnNode -> {
                owner = hokeInstruction.owner
                desc = hokeInstruction.desc
                name = hokeInstruction.name
            }
            is FieldInsnNode -> {
                owner = hokeInstruction.owner
                desc = hokeInstruction.desc
                name = hokeInstruction.name
            }
            else -> {
                throw RuntimeException("非法指令")
            }
        }
        val lintLog = StringBuilder()
        lintLog.append(classPath)
        lintLog.append("  ->  ")
        lintLog.append(methodName)
        lintLog.append("  ->  ")
        lintLog.append(methodDesc)
        lintLog.append("\n")
        lintLog.append(owner)
        lintLog.append("  ->  ")
        lintLog.append(name)
        lintLog.append("  ->  ")
        lintLog.append(desc)
        return lintLog
    }

    private fun insertRuntimeLog(
        classNode: ClassNode,
        methodNode: MethodNode,
        hokeInstruction: AbstractInsnNode
    ) {
        val lintLog = getLintLog(classNode, methodNode, hokeInstruction)
        lintLog.insert(0, "\n")
        val insnList = InsnList()
        insnList.add(LdcInsnNode(lintLog.toString()))
        insnList.add(
            MethodInsnNode(
                Opcodes.INVOKESTATIC,
                classNode.name,
                printStackTraceMethodName,
                printStackTraceMethodDesc
            )
        )
        methodNode.instructions.insert(insnList)
    }

    private fun generatePrintStackTraceMethod(
        classWriter: ClassWriter,
        runtimeRecord: PrivacySentryRuntimeRecord
    ) {
        val methodVisitor = classWriter.visitMethod(
            Opcodes.ACC_PRIVATE or Opcodes.ACC_STATIC,
            printStackTraceMethodName,
            printStackTraceMethodDesc,
            null,
            null
        )
        methodVisitor.visitCode()
        methodVisitor.visitTypeInsn(Opcodes.NEW, "java/lang/Throwable")
        methodVisitor.visitInsn(Opcodes.DUP)
        methodVisitor.visitMethodInsn(
            Opcodes.INVOKESPECIAL,
            "java/lang/Throwable",
            "<init>",
            "()V",
            false
        )
        methodVisitor.visitVarInsn(Opcodes.ASTORE, 1)
        methodVisitor.visitTypeInsn(Opcodes.NEW, "java/io/ByteArrayOutputStream")
        methodVisitor.visitInsn(Opcodes.DUP)
        methodVisitor.visitMethodInsn(
            Opcodes.INVOKESPECIAL,
            "java/io/ByteArrayOutputStream",
            "<init>",
            "()V",
            false
        )
        methodVisitor.visitVarInsn(Opcodes.ASTORE, 2)
        methodVisitor.visitVarInsn(Opcodes.ALOAD, 1)
        methodVisitor.visitTypeInsn(Opcodes.NEW, "java/io/PrintStream")
        methodVisitor.visitInsn(Opcodes.DUP)
        methodVisitor.visitVarInsn(Opcodes.ALOAD, 2)
        methodVisitor.visitMethodInsn(
            Opcodes.INVOKESPECIAL,
            "java/io/PrintStream",
            "<init>",
            "(Ljava/io/OutputStream;)V",
            false
        )
        methodVisitor.visitMethodInsn(
            Opcodes.INVOKEVIRTUAL,
            "java/lang/Throwable",
            "printStackTrace",
            "(Ljava/io/PrintStream;)V",
            false
        )
        methodVisitor.visitTypeInsn(Opcodes.NEW, "java/lang/StringBuilder")
        methodVisitor.visitInsn(Opcodes.DUP)
        methodVisitor.visitMethodInsn(
            Opcodes.INVOKESPECIAL,
            "java/lang/StringBuilder",
            "<init>",
            "()V",
            false
        )
        methodVisitor.visitVarInsn(Opcodes.ALOAD, 0)
        methodVisitor.visitMethodInsn(
            Opcodes.INVOKEVIRTUAL,
            "java/lang/StringBuilder",
            "append",
            "(Ljava/lang/String;)Ljava/lang/StringBuilder;",
            false
        )
        methodVisitor.visitVarInsn(Opcodes.ALOAD, 2)
        methodVisitor.visitMethodInsn(
            Opcodes.INVOKEVIRTUAL,
            "java/lang/StringBuilder",
            "append",
            "(Ljava/lang/Object;)Ljava/lang/StringBuilder;",
            false
        )
        methodVisitor.visitMethodInsn(
            Opcodes.INVOKEVIRTUAL,
            "java/lang/StringBuilder",
            "toString",
            "()Ljava/lang/String;",
            false
        )
        methodVisitor.visitVarInsn(Opcodes.ASTORE, 3)
        methodVisitor.visitVarInsn(Opcodes.ALOAD, 3)
        methodVisitor.visitMethodInsn(
            Opcodes.INVOKESTATIC,
            runtimeRecord.methodOwner,
            runtimeRecord.methodName,
            runtimeRecord.methodDesc,
            false
        )
        methodVisitor.visitInsn(Opcodes.RETURN)
        methodVisitor.visitMaxs(4, 4)
        methodVisitor.visitEnd()
    }

    override fun onTransformEnd() {
        if (allLintLog.isNotEmpty()) {
            FileUtils.write(generateLogFile(), allLintLog, Charset.defaultCharset())
        }
        runtimeRecord = null
    }

    private fun generateLogFile(): File {
        val time = SimpleDateFormat(
            "yyyy_MM_dd_HH_mm_ss",
            Locale.CHINA
        ).format(Date(System.currentTimeMillis()))
        return File(
            FileSystemView.getFileSystemView().homeDirectory,
            "PrivacySentry_${time}.txt"
        )
    }

}