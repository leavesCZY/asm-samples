package github.leavesc.asm.plugins.optimized_thread

import com.android.build.api.transform.QualifiedContent
import com.android.build.gradle.internal.pipeline.TransformManager
import github.leavesc.asm.base.BaseTransform
import github.leavesc.asm.utils.Log
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.MethodInsnNode
import org.objectweb.asm.tree.MethodNode

/**
 * @Author: leavesC
 * @Date: 2021/12/16 15:11
 * @Desc:
 */
class OptimizedThreadTransform : BaseTransform() {

    override fun modifyClass(byteArray: ByteArray): ByteArray {
        val classNode = ClassNode()
        val classReader = ClassReader(byteArray)
        classReader.accept(classNode, ClassReader.EXPAND_FRAMES)
        val methods = classNode.methods
        if (!methods.isNullOrEmpty()) {
            for (methodNode in methods) {
                val instructionIterator = methodNode.instructions?.iterator()
                if (instructionIterator != null) {
                    while (instructionIterator.hasNext()) {
                        val instruction = instructionIterator.next()
                        when (instruction.opcode) {
                            Opcodes.INVOKESTATIC -> {
                                transformInvokeStatic(
                                    classNode,
                                    methodNode,
                                    instruction as MethodInsnNode
                                )
                            }
                            Opcodes.NEW -> {
                                transformNew(
                                    classNode,
                                    methodNode,
                                    instruction as? MethodInsnNode
                                )
                            }
                        }
                    }
                }
            }
        }
        val classWriter = ClassWriter(ClassWriter.COMPUTE_MAXS)
        classNode.accept(classWriter)
        return classWriter.toByteArray()
    }

    private val EXECUTORS_CLASS = "java/util/concurrent/Executors"

    private fun transformInvokeStatic(
        classNode: ClassNode,
        methodNode: MethodNode,
        methodInsnNode: MethodInsnNode
    ) {
        if (methodInsnNode.owner == EXECUTORS_CLASS) {
            Log.log("methodInsnNode owner: " + methodInsnNode.owner)
            Log.log("methodInsnNode desc: " + methodInsnNode.desc)
            Log.log("methodInsnNode name: " + methodInsnNode.name)
            Log.log("methodInsnNode itf: " + methodInsnNode.itf)
            when (methodInsnNode.name) {
                "newFixedThreadPool" -> {
                    if (methodInsnNode.desc == "(I)Ljava/util/concurrent/ExecutorService;") {
                        methodInsnNode.owner =
                            "github/leavesc/asm/optimized_thread/OptimizedThreadPool"
                        methodInsnNode.name = "getExecutorService"
                    }
                }
            }
        }
    }

    private fun transformNew(
        classNode: ClassNode,
        methodNode: MethodNode,
        methodInsnNode: MethodInsnNode?
    ) {

    }

    override fun getInputTypes(): Set<QualifiedContent.ContentType> {
        return TransformManager.CONTENT_CLASS
    }

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
        return mutableSetOf(
            QualifiedContent.Scope.PROJECT,
            QualifiedContent.Scope.SUB_PROJECTS,
            QualifiedContent.Scope.EXTERNAL_LIBRARIES
        )
    }

    override fun getName(): String {
        return "OptimizedThreadTransform"
    }

    override fun isIncremental(): Boolean {
        return true
    }

}