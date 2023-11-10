package github.leavesczy.trace.utils

import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.InvokeDynamicInsnNode
import org.objectweb.asm.tree.MethodInsnNode
import org.objectweb.asm.tree.MethodNode

/**
 * @Author: leavesCZY
 * @Github: https://github.com/leavesCZY
 * @Desc:
 */
internal const val InitMethodName = "<init>"

internal val ClassNode.simpleClassName: String
    get() = name.substringAfterLast('/')

internal val MethodNode.nameWithDesc: String
    get() = name + desc

internal val MethodNode.isStatic: Boolean
    get() = access and Opcodes.ACC_STATIC != 0

internal val MethodNode.isInitMethod: Boolean
    get() = name == InitMethodName

internal fun MethodNode.hasAnnotation(annotationDesc: String): Boolean {
    return visibleAnnotations?.find { it.desc == annotationDesc } != null
}

internal fun MethodInsnNode.insertArgument(argumentType: Class<*>) {
    val type = Type.getMethodType(desc)
    val argumentTypes = type.argumentTypes
    val returnType = type.returnType
    val newArgumentTypes = arrayOfNulls<Type>(argumentTypes.size + 1)
    System.arraycopy(argumentTypes, 0, newArgumentTypes, 0, argumentTypes.size - 1 + 1)
    newArgumentTypes[newArgumentTypes.size - 1] = Type.getType(argumentType)
    desc = Type.getMethodDescriptor(returnType, *newArgumentTypes)
}

internal fun MethodNode.filterLambda(filter: (InvokeDynamicInsnNode) -> Boolean): List<InvokeDynamicInsnNode> {
    val mInstructions = instructions ?: return emptyList()
    val dynamicList = mutableListOf<InvokeDynamicInsnNode>()
    mInstructions.forEach { instruction ->
        if (instruction is InvokeDynamicInsnNode) {
            if (filter(instruction)) {
                dynamicList.add(instruction)
            }
        }
    }
    return dynamicList
}