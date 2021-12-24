package github.leavesc.asm.plugins.privacy_sentry

import github.leavesc.asm.base.BaseTransform
import github.leavesc.asm.utils.Log
import org.apache.commons.io.FileUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.FieldInsnNode
import org.objectweb.asm.tree.MethodInsnNode
import java.io.File
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

    private val logRecordList = mutableListOf<String>()

    private val logFile by lazy {
        val time = SimpleDateFormat(
            "yyyy_MM_dd_HH_mm_ss",
            Locale.CHINA
        ).format(Date(System.currentTimeMillis()))
        File(
            FileSystemView.getFileSystemView().homeDirectory,
            "PrivacySentry_$time.txt"
        )
    }

    override fun modifyClass(byteArray: ByteArray): ByteArray {
        val classNode = ClassNode()
        val classReader = ClassReader(byteArray)
        classReader.accept(classNode, ClassReader.EXPAND_FRAMES)
        val methods = classNode.methods
        if (!methods.isNullOrEmpty()) {
            for (methodNode in methods) {
                val instructions = methodNode.instructions
                val instructionIterator = instructions?.iterator()
                if (instructionIterator != null) {
                    while (instructionIterator.hasNext()) {
                        val node = instructionIterator.next()
                        if (node.isHookPoint(config)) {
                            writeToFile(classNode, node)
                        }
                    }
                }
            }
        }
        return byteArray
    }

    private fun AbstractInsnNode.isHookPoint(config: PrivacySentryConfig): Boolean {
        val methodHookPoints = config.methodHookPointList
        val fieldHookPoints = config.fieldHookPointList
        when (this) {
            is MethodInsnNode -> {
                val owner = this.owner
                val desc = this.desc
                val name = this.name
                val find = methodHookPoints.find {
                    it.owner == owner && it.desc == desc
                            && it.name == name
                }
                return find != null
            }
            is FieldInsnNode -> {
                val owner = this.owner
                val desc = this.desc
                val name = this.name
                val find = fieldHookPoints.find {
                    it.owner == owner && it.desc == desc && it.name == name
                }
                return find != null
            }
        }
        return false
    }

    private fun writeToFile(classNode: ClassNode, node: AbstractInsnNode) {
        val classPath = classNode.name
        val owner: String
        val desc: String
        val name: String
        when (node) {
            is MethodInsnNode -> {
                owner = node.owner
                desc = node.desc
                name = node.name
            }
            is FieldInsnNode -> {
                owner = node.owner
                desc = node.desc
                name = node.name
            }
            else -> {
                throw RuntimeException("非法指令")
            }
        }
        Log.log(
            "PrivacySentryTransform $classPath _ $owner _ $desc $name"
        )
        logRecordList.clear()
        logRecordList.add(classPath)
        logRecordList.add(owner)
        logRecordList.add(desc)
        logRecordList.add(name)
        logRecordList.add("\n")
        FileUtils.writeLines(logFile, logRecordList, true)
    }

    override fun onTransformStart() {
        Log.log(
            "PrivacySentryTransform onTransformStart"
        )
    }

    override fun onTransformEnd() {
        Log.log(
            "PrivacySentryTransform onTransformEnd"
        )
    }

}