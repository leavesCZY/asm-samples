package github.leavesczy.asm.plugins.doubleClick.compose

import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.InstrumentationParameters
import github.leavesczy.asm.utils.LogPrint
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.*


/**
 * @Author: CZY
 * @Date: 2022/9/24 15:07
 * @Desc:
 */
interface ComposeDoubleClickConfigParameters : InstrumentationParameters {
    @get:Input
    val config: Property<ComposeDoubleClickConfig>
}

abstract class ComposeDoubleClickClassVisitorFactory :
    AsmClassVisitorFactory<ComposeDoubleClickConfigParameters> {

    override fun createClassVisitor(
        classContext: ClassContext,
        nextClassVisitor: ClassVisitor
    ): ClassVisitor {
        return ComposeDoubleClickClassVisitor(
            nextClassVisitor = nextClassVisitor,
            classData = classContext.currentClassData,
            config = parameters.get().config.get()
        )
    }

    override fun isInstrumentable(classData: ClassData): Boolean {
        return true
    }

}

private class ComposeDoubleClickClassVisitor(
    private val nextClassVisitor: ClassVisitor,
    private val classData: ClassData,
    private val config: ComposeDoubleClickConfig,
) :
    ClassNode(Opcodes.ASM5) {

    private companion object {

        private const val ComposeClickClassName = "androidx.compose.foundation.ClickableKt"

        private const val ClickableMethodDesc =
            "(Landroidx/compose/ui/Modifier;Landroidx/compose/foundation/interaction/MutableInteractionSource;Landroidx/compose/foundation/Indication;ZLjava/lang/String;Landroidx/compose/ui/semantics/Role;Lkotlin/jvm/functions/Function0;)Landroidx/compose/ui/Modifier;"

        private const val CombinedClickableMethodDesc =
            "(Landroidx/compose/ui/Modifier;Landroidx/compose/foundation/interaction/MutableInteractionSource;Landroidx/compose/foundation/Indication;ZLjava/lang/String;Landroidx/compose/ui/semantics/Role;Ljava/lang/String;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;)Landroidx/compose/ui/Modifier;"

    }

    override fun visitEnd() {
        super.visitEnd()
        if (classData.className == ComposeClickClassName) {
            methods.forEach { methodNode ->
                val methodName = methodNode.name
                val methodDesc = methodNode.desc
                LogPrint.log("methodName: $methodName \n methodDesc: $methodDesc")
                LogPrint.log("\n\n")
                val onClickArgumentIndex = when (methodDesc) {
                    ClickableMethodDesc -> {
                        6
                    }
                    CombinedClickableMethodDesc -> {
                        9
                    }
                    else -> {
                        -1
                    }
                }
                if (onClickArgumentIndex > 0) {
                    val onClickLabelArgumentIndex = 4
                    val input = InsnList()
                    input.add(LdcInsnNode(config.whiteListTag))
                    input.add(VarInsnNode(Opcodes.ALOAD, onClickLabelArgumentIndex))
                    input.add(
                        MethodInsnNode(
                            Opcodes.INVOKEVIRTUAL,
                            "java/lang/String",
                            "equals",
                            "(Ljava/lang/Object;)Z",
                            false
                        )
                    )
                    val label = LabelNode()
                    input.add(JumpInsnNode(Opcodes.IFNE, label))
                    input.add(TypeInsnNode(Opcodes.NEW, config.formatOnClickWrapClass))
                    input.add(InsnNode(Opcodes.DUP))
                    input.add(VarInsnNode(Opcodes.ALOAD, onClickArgumentIndex))
                    input.add(
                        MethodInsnNode(
                            Opcodes.INVOKESPECIAL,
                            config.formatOnClickWrapClass,
                            "<init>",
                            "(Lkotlin/jvm/functions/Function0;)V",
                            false
                        )
                    )
                    input.add(VarInsnNode(Opcodes.ASTORE, onClickArgumentIndex))
                    input.add(label)
                    methodNode.instructions.insert(input)
                }
            }
        }
        accept(nextClassVisitor)
    }

}