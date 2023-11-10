package github.leavesczy.trace.plugins.click.compose

import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.InstrumentationParameters
import github.leavesczy.trace.utils.LogPrint
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.InsnNode
import org.objectweb.asm.tree.JumpInsnNode
import org.objectweb.asm.tree.LabelNode
import org.objectweb.asm.tree.LdcInsnNode
import org.objectweb.asm.tree.MethodInsnNode
import org.objectweb.asm.tree.TypeInsnNode
import org.objectweb.asm.tree.VarInsnNode


/**
 * @Author: leavesCZY
 * @Github: https://github.com/leavesCZY
 * @Desc:
 */
internal interface ComposeClickConfigParameters : InstrumentationParameters {
    @get:Input
    val config: Property<ComposeClickConfig>
}

internal abstract class ComposeClickClassVisitorFactory :
    AsmClassVisitorFactory<ComposeClickConfigParameters> {

    override fun createClassVisitor(
        classContext: ClassContext,
        nextClassVisitor: ClassVisitor
    ): ClassVisitor {
        return ComposeClickClassVisitor(
            nextClassVisitor = nextClassVisitor,
            classData = classContext.currentClassData,
            config = parameters.get().config.get()
        )
    }

    override fun isInstrumentable(classData: ClassData): Boolean {
        return true
    }

}

private class ComposeClickClassVisitor(
    private val nextClassVisitor: ClassVisitor,
    private val classData: ClassData,
    private val config: ComposeClickConfig,
) : ClassNode(Opcodes.ASM5) {

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
                val methodDesc = methodNode.desc
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
                    input.add(TypeInsnNode(Opcodes.NEW, config.onClickClass))
                    input.add(InsnNode(Opcodes.DUP))
                    input.add(VarInsnNode(Opcodes.ALOAD, onClickArgumentIndex))
                    input.add(
                        MethodInsnNode(
                            Opcodes.INVOKESPECIAL,
                            config.onClickClass,
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