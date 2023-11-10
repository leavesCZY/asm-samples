package github.leavesczy.trace.plugins.bitmap

import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.InstrumentationParameters
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode

/**
 * @Author: leavesCZY
 * @Github: https://github.com/leavesCZY
 * @Desc:
 */
internal interface LegalBitmapConfigParameters : InstrumentationParameters {
    @get:Input
    val config: Property<LegalBitmapConfig>
}

internal abstract class LegalBitmapClassVisitorFactory :
    AsmClassVisitorFactory<LegalBitmapConfigParameters> {

    override fun createClassVisitor(
        classContext: ClassContext,
        nextClassVisitor: ClassVisitor
    ): ClassVisitor {
        return LegalBitmapClassVisitor(
            config = parameters.get().config.get(),
            nextClassVisitor = nextClassVisitor
        )
    }

    override fun isInstrumentable(classData: ClassData): Boolean {
        return true
    }

}

private class LegalBitmapClassVisitor(
    private val config: LegalBitmapConfig,
    private val nextClassVisitor: ClassVisitor
) : ClassNode(Opcodes.ASM5) {

    private companion object {

        private const val ImageViewClass = "android/widget/ImageView"

    }

    override fun visit(
        version: Int,
        access: Int,
        name: String?,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        if (superName == ImageViewClass && name != config.imageViewClass) {
            super.visit(
                version,
                access,
                name,
                signature,
                config.imageViewClass,
                interfaces
            )
        } else {
            super.visit(version, access, name, signature, superName, interfaces)
        }
    }

    override fun visitEnd() {
        super.visitEnd()
        accept(nextClassVisitor)
    }

}