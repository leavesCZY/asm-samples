package github.leavesczy.asm.plugins.double_click

import com.android.build.api.transform.QualifiedContent
import com.android.build.gradle.internal.pipeline.TransformManager
import github.leavesczy.asm.base.BaseTransform
import github.leavesczy.asm.utils.*
import org.objectweb.asm.*
import org.objectweb.asm.tree.*

/**
 * @Author: leavesCZY
 * @Date: 2021/12/2 16:59
 * @Desc:
 */
class DoubleClickTransform(private val config: DoubleClickConfig) : BaseTransform() {

    private companion object {

        private const val ViewDescriptor = "Landroid/view/View;"

        private const val OnClickViewMethodDescriptor = "(Landroid/view/View;)V"

        private const val ButterKnifeOnClickAnnotationDesc = "Lbutterknife/OnClick;"

        private val MethodNode.onlyOneViewParameter: Boolean
            get() = desc == OnClickViewMethodDescriptor

        private fun MethodNode.hasCheckViewAnnotation(config: DoubleClickConfig): Boolean {
            return hasAnnotation(config.formatCheckViewOnClickAnnotation)
        }

        private fun MethodNode.hasUncheckViewOnClickAnnotation(config: DoubleClickConfig): Boolean {
            return hasAnnotation(config.formatUncheckViewOnClickAnnotation)
        }

        private fun MethodNode.hasButterKnifeOnClickAnnotation(): Boolean {
            return hasAnnotation(ButterKnifeOnClickAnnotationDesc)
        }

    }

    override fun modifyClass(byteArray: ByteArray): ByteArray {
        val classReader = ClassReader(byteArray)
        val classNode = ClassNode()
        classReader.accept(classNode, ClassReader.EXPAND_FRAMES)
        val methods = classNode.methods
        if (!methods.isNullOrEmpty()) {
            val shouldHookMethodList = mutableSetOf<String>()
            for (methodNode in methods) {
                //静态、包含 UncheckViewOnClick 注解的方法不用处理
                if (methodNode.isStatic ||
                    methodNode.hasUncheckViewOnClickAnnotation(config)
                ) {
                    continue
                }
                val methodNameWithDesc = methodNode.nameWithDesc
                if (methodNode.onlyOneViewParameter) {
                    if (methodNode.hasCheckViewAnnotation(config)) {
                        //添加了 CheckViewOnClick 注解的情况
                        shouldHookMethodList.add(methodNameWithDesc)
                        continue
                    } else if (methodNode.hasButterKnifeOnClickAnnotation()) {
                        //使用了 ButterKnife，且当前 method 添加了 OnClick 注解
                        shouldHookMethodList.add(methodNameWithDesc)
                        continue
                    }
                }
                if (classNode.isHookPoint(config, methodNode)) {
                    shouldHookMethodList.add(methodNameWithDesc)
                    continue
                }
                //判断方法内部是否有需要处理的 lambda 表达式
                val invokeDynamicInsnNodes = methodNode.findHookPointLambda(config)
                invokeDynamicInsnNodes.forEach {
                    val handle = it.bsmArgs[1] as? Handle
                    if (handle != null) {
                        shouldHookMethodList.add(handle.name + handle.desc)
                    }
                }
            }
            if (shouldHookMethodList.isNotEmpty()) {
                for (methodNode in methods) {
                    val methodNameWithDesc = methodNode.nameWithDesc
                    if (shouldHookMethodList.contains(methodNameWithDesc)) {
                        val argumentTypes = Type.getArgumentTypes(methodNode.desc)
                        val viewArgumentIndex = argumentTypes?.indexOfFirst {
                            it.descriptor == ViewDescriptor
                        } ?: -1
                        if (viewArgumentIndex >= 0) {
                            val instructions = methodNode.instructions
                            if (instructions != null && instructions.size() > 0) {
                                val list = InsnList()
                                list.add(
                                    VarInsnNode(
                                        Opcodes.ALOAD, getVisitPosition(
                                            argumentTypes,
                                            viewArgumentIndex,
                                            methodNode.isStatic
                                        )
                                    )
                                )
                                list.add(
                                    MethodInsnNode(
                                        Opcodes.INVOKESTATIC,
                                        config.formatDoubleCheckClass,
                                        config.doubleCheckMethodName,
                                        config.doubleCheckMethodDescriptor
                                    )
                                )
                                val labelNode = LabelNode()
                                list.add(JumpInsnNode(Opcodes.IFNE, labelNode))
                                list.add(InsnNode(Opcodes.RETURN))
                                list.add(labelNode)
                                instructions.insert(list)
                            }
                        }
                    }
                }
                val classWriter = ClassWriter(ClassWriter.COMPUTE_MAXS)
                classNode.accept(classWriter)
                return classWriter.toByteArray()
            }
        }
        return byteArray
    }

    override fun getInputTypes(): Set<QualifiedContent.ContentType> {
        return TransformManager.CONTENT_CLASS
    }

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
        return mutableSetOf(
            QualifiedContent.Scope.PROJECT,
            QualifiedContent.Scope.SUB_PROJECTS,
//            QualifiedContent.Scope.EXTERNAL_LIBRARIES
        )
    }

}