package github.leavesc.asm.plugins.optimized_thread

import com.android.build.api.transform.QualifiedContent
import com.android.build.gradle.internal.pipeline.TransformManager
import github.leavesc.asm.base.BaseTransform
import github.leavesc.asm.utils.Log
import github.leavesc.asm.utils.insertArgument
import github.leavesc.asm.utils.nameWithDesc
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.*

/**
 * @Author: leavesC
 * @Date: 2021/12/16 15:11
 * @Desc:
 */
class OptimizedThreadTransform(private val config: OptimizedThreadConfig) : BaseTransform() {

    override fun modifyClass(byteArray: ByteArray): ByteArray {
        val classNode = ClassNode()
        val classReader = ClassReader(byteArray)
        classReader.accept(classNode, ClassReader.EXPAND_FRAMES)
        val methods = classNode.methods
        val taskList = mutableListOf<() -> Unit>()
        if (!methods.isNullOrEmpty()) {
            for (methodNode in methods) {
                val instructionIterator = methodNode.instructions?.iterator()
                if (instructionIterator != null) {
                    while (instructionIterator.hasNext()) {
                        val instruction = instructionIterator.next()
                        when (instruction.opcode) {
                            Opcodes.INVOKESTATIC -> {
                                val methodInsnNode = instruction as? MethodInsnNode
                                if (methodInsnNode?.owner == config.executorsClass) {
                                    taskList.add {
                                        transformInvokeStatic(
                                            classNode,
                                            methodNode,
                                            instruction
                                        )
                                    }
                                }
                            }
                            Opcodes.NEW -> {
                                val typeInsnNode = instruction as? TypeInsnNode
                                if (typeInsnNode?.desc == config.threadClass) {
                                    if (!classNode.isThreadFactoryMethod(methodNode)) {
                                        Log.log("处理")
                                        taskList.add {
                                            transformNew(
                                                classNode,
                                                methodNode,
                                                instruction
                                            )
                                        }
                                    }else{
                                        Log.log("不处理")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        taskList.forEach {
            it.invoke()
        }
        val classWriter = ClassWriter(ClassWriter.COMPUTE_MAXS)
        classNode.accept(classWriter)
        return classWriter.toByteArray()
    }

    private fun transformInvokeStatic(
        classNode: ClassNode,
        methodNode: MethodNode,
        methodInsnNode: MethodInsnNode
    ) {
        val pointMethod = config.threadHookPointList.find { it.methodName == methodInsnNode.name }
        if (pointMethod != null) {
            methodInsnNode.owner = config.formatOptimizedThreadPoolClass
            methodInsnNode.insertArgument(String::class.java)
            methodNode.instructions.insertBefore(
                methodInsnNode,
                LdcInsnNode(classNode.name.substringAfterLast('/'))
            )
        }
    }

    private fun transformNew(
        classNode: ClassNode,
        methodNode: MethodNode,
        typeInsnNode: TypeInsnNode
    ) {
        val instructions = methodNode.instructions
        val typeInsnNodeIndex = instructions.indexOf(typeInsnNode)
        for (index in typeInsnNodeIndex + 1 until instructions.size()) {
            val node = instructions[index]
            if (node is MethodInsnNode && node.isThreadInitMethodInsn()) {
                typeInsnNode.desc = config.formatOptimizedThreadClass
                instructions.insertBefore(node, LdcInsnNode(classNode.name.substringAfterLast('/')))
                node.insertArgument(String::class.java)
                node.owner = config.formatOptimizedThreadClass
                break
            }
        }
    }

    private fun AbstractInsnNode.isThreadInitMethodInsn(): Boolean {
        return this is MethodInsnNode && this.owner == "java/lang/Thread"
                && this.name == "<init>"
    }

    private fun ClassNode.isThreadFactoryMethod(methodNode: MethodNode): Boolean {
        return this.interfaces?.contains("java/util/concurrent/ThreadFactory") == true
                && methodNode.nameWithDesc == "newThread(Ljava/lang/Runnable;)Ljava/lang/Thread;"
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