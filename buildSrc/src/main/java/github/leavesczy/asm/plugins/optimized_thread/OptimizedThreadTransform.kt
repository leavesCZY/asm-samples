package github.leavesczy.asm.plugins.optimized_thread

import github.leavesczy.asm.base.BaseTransform
import github.leavesczy.asm.utils.insertArgument
import github.leavesczy.asm.utils.nameWithDesc
import github.leavesczy.asm.utils.simpleClassName
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.*

/**
 * @Author: leavesCZY
 * @Date: 2021/12/16 15:11
 * @Desc:
 * @Github：https://github.com/leavesCZY
 */
class OptimizedThreadTransform(private val config: OptimizedThreadConfig) : BaseTransform() {

    companion object {

        private const val executorsClass = "java/util/concurrent/Executors"

        private const val threadClass = "java/lang/Thread"

        private const val threadFactoryClass = "java/util/concurrent/ThreadFactory"

        private const val threadFactoryNewThreadMethodDesc =
            "newThread(Ljava/lang/Runnable;)Ljava/lang/Thread;"

    }

    override fun modifyClass(byteArray: ByteArray): ByteArray {
        val classNode = ClassNode()
        val classReader = ClassReader(byteArray)
        classReader.accept(classNode, ClassReader.EXPAND_FRAMES)
        val methods = classNode.methods
        if (!methods.isNullOrEmpty()) {
            val taskList = mutableListOf<() -> Unit>()
            for (methodNode in methods) {
                val instructionIterator = methodNode.instructions?.iterator()
                if (instructionIterator != null) {
                    while (instructionIterator.hasNext()) {
                        val instruction = instructionIterator.next()
                        when (instruction.opcode) {
                            Opcodes.INVOKESTATIC -> {
                                val methodInsnNode = instruction as? MethodInsnNode
                                if (methodInsnNode?.owner == executorsClass) {
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
                                if (typeInsnNode?.desc == threadClass) {
                                    //如果是在 ThreadFactory 内初始化线程，则不处理
                                    if (!classNode.isThreadFactoryMethod(methodNode)) {
                                        taskList.add {
                                            transformNew(
                                                classNode,
                                                methodNode,
                                                instruction
                                            )
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
            //将 Executors 替换为 OptimizedThreadPool
            methodInsnNode.owner = config.formatOptimizedThreadPoolClass
            //为调用 newFixedThreadPool 等方法的指令多插入一个 String 类型的方法入参参数声明
            methodInsnNode.insertArgument(String::class.java)
            //将 ClassName 作为入参参数传给 newFixedThreadPool 等方法
            methodNode.instructions.insertBefore(
                methodInsnNode,
                LdcInsnNode(classNode.simpleClassName)
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
        //从 typeInsnNode 指令开始遍历，找到调用 Thread 构造函数的指令，然后对其进行替换
        for (index in typeInsnNodeIndex + 1 until instructions.size()) {
            val node = instructions[index]
            if (node is MethodInsnNode && node.isThreadInitMethodInsn()) {
                //将 Thread 替换为 OptimizedThread
                typeInsnNode.desc = config.formatOptimizedThreadClass
                node.owner = config.formatOptimizedThreadClass
                //为调用 Thread 构造函数的指令多插入一个 String 类型的方法入参参数声明
                node.insertArgument(String::class.java)
                //将 ClassName 作为构造参数传给 OptimizedThread
                instructions.insertBefore(node, LdcInsnNode(classNode.simpleClassName))
                break
            }
        }
    }

    private fun MethodInsnNode.isThreadInitMethodInsn(): Boolean {
        return this.owner == threadClass && this.name == "<init>"
    }

    private fun ClassNode.isThreadFactoryMethod(methodNode: MethodNode): Boolean {
        return this.interfaces?.contains(threadFactoryClass) == true
                && methodNode.nameWithDesc == threadFactoryNewThreadMethodDesc
    }

}