package github.leavesc.asm.plugins.privacy_sentry

import github.leavesc.asm.base.BaseTransform
import github.leavesc.asm.utils.Log
import github.leavesc.asm.utils.simpleClassName
import org.objectweb.asm.ClassReader
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.FieldInsnNode
import org.objectweb.asm.tree.MethodInsnNode

/**
 * @Author: leavesC
 * @Date: 2021/12/21 22:58
 * @Desc:
 * @Github：https://github.com/leavesC
 */
class PrivacySentryTransform(private val config: PrivacySentryConfig) : BaseTransform() {

    override fun modifyClass(byteArray: ByteArray): ByteArray {
        val classNode = ClassNode()
        val classReader = ClassReader(byteArray)
        classReader.accept(classNode, ClassReader.EXPAND_FRAMES)
        val methods = classNode.methods
        val methodHookPoints = config.methodHookPointList
        val fieldHookPoints = config.fieldHookPointList
        if (!methods.isNullOrEmpty()) {
            for (methodNode in methods) {
                val instructions = methodNode.instructions
                val instructionIterator = instructions?.iterator()
                if (instructionIterator != null) {
                    while (instructionIterator.hasNext()) {
                        when (val instruction = instructionIterator.next()) {
                            is MethodInsnNode -> {
                                val owner = instruction.owner
                                val desc = instruction.desc
                                val name = instruction.name
                                val find = methodHookPoints.find {
                                    it.owner == owner && it.desc == desc
                                            && it.name == name
                                }
                                if (find != null) {
                                    Log.log(
                                        "PrivacySentryTransform " + classNode.simpleClassName
                                                + " _ " + owner + " _ " + desc + " " + name
                                    )
                                }
                            }
                            is FieldInsnNode -> {
                                val owner = instruction.owner
                                val desc = instruction.desc
                                val name = instruction.name
                                val find = fieldHookPoints.find {
                                    it.owner == owner && it.desc == desc
                                            && it.name == name
                                }
                                if (find != null) {
                                    Log.log(
                                        "PrivacySentryTransform " + classNode.simpleClassName
                                                + " _ " + owner + " _ " + desc + " " + name
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        return byteArray
    }

}