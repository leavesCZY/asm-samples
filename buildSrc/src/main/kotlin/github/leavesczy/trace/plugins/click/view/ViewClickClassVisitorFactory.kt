package github.leavesczy.trace.plugins.click.view

import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.InstrumentationParameters
import github.leavesczy.trace.utils.filterLambda
import github.leavesczy.trace.utils.hasAnnotation
import github.leavesczy.trace.utils.isStatic
import github.leavesczy.trace.utils.nameWithDesc
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Handle
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.InsnNode
import org.objectweb.asm.tree.JumpInsnNode
import org.objectweb.asm.tree.LabelNode
import org.objectweb.asm.tree.MethodInsnNode
import org.objectweb.asm.tree.MethodNode
import org.objectweb.asm.tree.VarInsnNode

/**
 * @Author: leavesCZY
 * @Github: https://github.com/leavesCZY
 * @Desc:
 */
internal interface ViewClickConfigParameters : InstrumentationParameters {
    @get:Input
    val config: Property<ViewClickConfig>
}

internal abstract class ViewClickClassVisitorFactory :
    AsmClassVisitorFactory<ViewClickConfigParameters> {

    override fun createClassVisitor(
        classContext: ClassContext,
        nextClassVisitor: ClassVisitor
    ): ClassVisitor {
        val config = parameters.get().config.get()
        val include = config.include
        val className = classContext.currentClassData.className
        if (include.any {
                className.startsWith(prefix = it)
            }
        ) {
            return ViewClickClassVisitor(
                nextClassVisitor = nextClassVisitor,
                config = config
            )
        }
        return nextClassVisitor
    }

    override fun isInstrumentable(classData: ClassData): Boolean {
        return true
    }

}

private class ViewClickClassVisitor(
    private val nextClassVisitor: ClassVisitor,
    private val config: ViewClickConfig,
) : ClassNode(Opcodes.ASM5) {

    private companion object {

        private const val ViewDescriptor = "Landroid/view/View;"

        private const val ButterKnifeOnClickAnnotationDesc = "Lbutterknife/OnClick;"

    }

    override fun visitEnd() {
        super.visitEnd()
        val shouldHookMethodList = mutableSetOf<MethodNode>()
        methods.forEach { methodNode ->
            when {
                methodNode.isStatic -> {
                    //不处理静态方法
                }

                methodNode.hasUncheckViewOnClickAnnotation() -> {
                    //不处理包含 UncheckViewOnClick 注解的方法
                }

                methodNode.hasCheckViewAnnotation() -> {
                    //使用了 CheckViewOnClick 注解的情况
                    shouldHookMethodList.add(methodNode)
                }

                methodNode.hasButterKnifeOnClickAnnotation() -> {
                    //使用了 ButterKnife OnClick 注解的情况
                    shouldHookMethodList.add(methodNode)
                }

                methodNode.isHookPoint() -> {
                    //使用了匿名内部类的情况
                    shouldHookMethodList.add(methodNode)
                }
            }
            //判断方法内部是否有需要处理的 lambda 表达式
            val dynamicInsnNodes = methodNode.filterLambda {
                val nodeName = it.name
                val nodeDesc = it.desc
                val find = config.hookPointList.find { point ->
                    nodeName == point.methodName && nodeDesc.endsWith(point.interfaceSignSuffix)
                }
                find != null
            }
            dynamicInsnNodes.forEach {
                val handle = it.bsmArgs[1] as? Handle
                if (handle != null) {
                    //找到 lambda 指向的目标方法
                    val nameWithDesc = handle.name + handle.desc
                    val method = methods.find { it.nameWithDesc == nameWithDesc }!!
                    shouldHookMethodList.add(method)
                }
            }
        }
        shouldHookMethodList.forEach {
            hookMethod(modeNode = it)
        }
        accept(nextClassVisitor)
    }

    private fun hookMethod(modeNode: MethodNode) {
        val argumentTypes = Type.getArgumentTypes(modeNode.desc)
        val viewArgumentIndex = argumentTypes?.indexOfFirst {
            it.descriptor == ViewDescriptor
        } ?: -1
        if (viewArgumentIndex >= 0) {
            val instructions = modeNode.instructions
            if (instructions != null && instructions.size() > 0) {
                val list = InsnList()
                list.add(
                    VarInsnNode(
                        Opcodes.ALOAD, getVisitPosition(
                            argumentTypes,
                            viewArgumentIndex,
                            modeNode.isStatic
                        )
                    )
                )
                list.add(
                    MethodInsnNode(
                        Opcodes.INVOKESTATIC,
                        config.clickCheckClass,
                        config.onClickMethodName,
                        config.onClickMethodDesc
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

    private fun getVisitPosition(
        argumentTypes: Array<Type>,
        parameterIndex: Int,
        isStaticMethod: Boolean
    ): Int {
        if (parameterIndex < 0 || parameterIndex >= argumentTypes.size) {
            throw Error("getVisitPosition error")
        }
        return if (parameterIndex == 0) {
            if (isStaticMethod) {
                0
            } else {
                1
            }
        } else {
            getVisitPosition(
                argumentTypes,
                parameterIndex - 1,
                isStaticMethod
            ) + argumentTypes[parameterIndex - 1].size
        }
    }

    private fun MethodNode.isHookPoint(): Boolean {
        val myInterfaces = interfaces
        if (myInterfaces.isNullOrEmpty()) {
            return false
        }
        val extraHookMethodList = config.hookPointList
        extraHookMethodList.forEach {
            if (myInterfaces.contains(it.interfaceName) && this.nameWithDesc == it.nameWithDesc) {
                return true
            }
        }
        return false
    }

    private fun MethodNode.hasCheckViewAnnotation(): Boolean {
        return hasAnnotation(config.checkViewOnClickAnnotation)
    }

    private fun MethodNode.hasUncheckViewOnClickAnnotation(): Boolean {
        return hasAnnotation(config.uncheckViewOnClickAnnotation)
    }

    private fun MethodNode.hasButterKnifeOnClickAnnotation(): Boolean {
        return hasAnnotation(ButterKnifeOnClickAnnotationDesc)
    }

}