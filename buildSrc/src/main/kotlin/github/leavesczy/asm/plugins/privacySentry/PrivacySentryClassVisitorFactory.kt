package github.leavesczy.asm.plugins.privacySentry

import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.InstrumentationParameters
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.swing.filechooser.FileSystemView

/**
 * @Author: leavesCZY
 * @Date: 2021/12/21 22:58
 * @Desc:
 * @Github：https://github.com/leavesCZY
 */
interface PrivacySentryConfigParameters : InstrumentationParameters {
    @get:Input
    val config: Property<PrivacySentryConfig>
}

abstract class PrivacySentryClassVisitorFactory :
    AsmClassVisitorFactory<PrivacySentryConfigParameters> {

    override fun createClassVisitor(
        classContext: ClassContext,
        nextClassVisitor: ClassVisitor
    ): ClassVisitor {
        return PrivacySentryClassVisitor(
            config = parameters.get().config.get(),
            nextClassVisitor = nextClassVisitor
        )
    }

    override fun isInstrumentable(classData: ClassData): Boolean {
        return true
    }

}

private class PrivacySentryClassVisitor(
    private val config: PrivacySentryConfig,
    private val nextClassVisitor: ClassVisitor
) : ClassNode(Opcodes.ASM5) {

    companion object {

        private const val writeToFileMethodName = "writeToFile"

        private const val writeToFileMethodDesc = "(Ljava/lang/String;Ljava/lang/Throwable;)V"

    }

    private val allLintLog = StringBuffer()

    private var runtimeRecord: PrivacySentryRuntimeRecord? = null

    override fun visit(
        version: Int,
        access: Int,
        name: String?,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        super.visit(version, access, name, signature, superName, interfaces)
        runtimeRecord = PrivacySentryConfig.runtimeRecord
    }

    override fun visitEnd() {
        super.visitEnd()
        modifyClass()
        if (allLintLog.isNotEmpty()) {
            //输出检测结果到桌面
//            FileUtils.write(generateLogFile(), allLintLog, Charset.defaultCharset())
        }
        accept(nextClassVisitor)
    }

    private fun modifyClass() {
        val mRuntimeRecord = runtimeRecord
        val taskList = mutableListOf<() -> Unit>()
        val tempLintLog = StringBuilder()
        methods.forEach { methodNode ->
            val instructions = methodNode.instructions
            val instructionIterator = instructions?.iterator()
            if (instructionIterator != null) {
                while (instructionIterator.hasNext()) {
                    val instruction = instructionIterator.next()
                    if (instruction.isHookPoint()) {
                        val lintLog = getLintLog(
                            methodNode,
                            instruction
                        )
                        tempLintLog.append(lintLog)
                        tempLintLog.append("\n\n")

                        if (mRuntimeRecord != null) {
                            taskList.add {
                                insertRuntimeLog(
                                    methodNode,
                                    instruction
                                )
                            }
                        }
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
            generateWriteToFileMethod(mRuntimeRecord)
        }
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
        methodNode: MethodNode,
        hokeInstruction: AbstractInsnNode
    ): StringBuilder {
        val className = name
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
        lintLog.append(className)
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
        methodNode: MethodNode,
        hokeInstruction: AbstractInsnNode
    ) {
        val insnList = InsnList()
        val lintLog = getLintLog(methodNode, hokeInstruction)
        lintLog.append("\n")
        insnList.add(LdcInsnNode(lintLog.toString()))
        insnList.add(TypeInsnNode(Opcodes.NEW, "java/lang/Throwable"))
        insnList.add(InsnNode(Opcodes.DUP))
        insnList.add(
            MethodInsnNode(
                Opcodes.INVOKESPECIAL, "java/lang/Throwable",
                "<init>", "()V", false
            )
        )
        insnList.add(
            MethodInsnNode(
                Opcodes.INVOKESTATIC,
                name,
                writeToFileMethodName,
                writeToFileMethodDesc
            )
        )
        methodNode.instructions.insertBefore(hokeInstruction, insnList)
    }

    private fun generateWriteToFileMethod(runtimeRecord: PrivacySentryRuntimeRecord) {
        val methodVisitor = visitMethod(
            Opcodes.ACC_PRIVATE or Opcodes.ACC_STATIC,
            writeToFileMethodName,
            writeToFileMethodDesc,
            null,
            null
        )
        methodVisitor.visitCode()
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
        methodVisitor.visitVarInsn(Opcodes.ALOAD, 2)
        methodVisitor.visitMethodInsn(
            Opcodes.INVOKEVIRTUAL,
            "java/io/ByteArrayOutputStream",
            "toString",
            "()Ljava/lang/String;",
            false
        )
        methodVisitor.visitVarInsn(Opcodes.ASTORE, 3)
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
        methodVisitor.visitVarInsn(Opcodes.ALOAD, 3)
        methodVisitor.visitMethodInsn(
            Opcodes.INVOKEVIRTUAL,
            "java/lang/StringBuilder",
            "append",
            "(Ljava/lang/String;)Ljava/lang/StringBuilder;",
            false
        )
        methodVisitor.visitMethodInsn(
            Opcodes.INVOKEVIRTUAL,
            "java/lang/StringBuilder",
            "toString",
            "()Ljava/lang/String;",
            false
        )
        methodVisitor.visitVarInsn(Opcodes.ASTORE, 4)
        methodVisitor.visitVarInsn(Opcodes.ALOAD, 4)
        methodVisitor.visitMethodInsn(
            Opcodes.INVOKESTATIC,
            runtimeRecord.methodOwner,
            runtimeRecord.methodName,
            runtimeRecord.methodDesc,
            false
        )
        methodVisitor.visitInsn(Opcodes.RETURN)
        methodVisitor.visitMaxs(4, 5)
        methodVisitor.visitEnd()
    }

    private fun generateLogFile(): File {
        val time = SimpleDateFormat(
            "yyyy_MM_dd_HH_mm_ss",
            Locale.CHINA
        ).format(Date(System.currentTimeMillis()))
        return File(
            FileSystemView.getFileSystemView().homeDirectory,
            "PrivacySentry" + File.separator + "PrivacySentry_${time}.txt"
        )
    }

}