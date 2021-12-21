package github.leavesc.asm.plugins.privacy_sentry

import github.leavesc.asm.base.BaseTransform
import github.leavesc.asm.utils.Log
import github.leavesc.asm.utils.simpleClassName
import org.objectweb.asm.ClassReader
import org.objectweb.asm.tree.ClassNode
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
        val hookPoints = config.hookPointList
        if (!methods.isNullOrEmpty()) {
            for (methodNode in methods) {
                val instructionIterator = methodNode.instructions?.iterator()
                if (instructionIterator != null) {
                    while (instructionIterator.hasNext()) {
                        val instruction = instructionIterator.next()
                        if (instruction is MethodInsnNode) {
                            val owner = instruction.owner
                            val desc = instruction.desc
                            val name = instruction.name
                            val find = hookPoints.find {
                                it.methodOwner == owner && it.methodDesc == desc
                                        && it.methodName == name
                            }
                            if (find != null) {
                                Log.log(
                                    " PrivacySentryTransform " + classNode.simpleClassName
                                            + " _ " + owner + " _ " + desc + " " + name
                                )
                            }
                        }
                    }
                }
            }
        }
        return byteArray
    }

}