package github.leavesczy.asm.utils

import github.leavesczy.asm.plugins.double_click.DoubleClickConfig
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.InvokeDynamicInsnNode
import org.objectweb.asm.tree.MethodInsnNode
import org.objectweb.asm.tree.MethodNode

/**
 * @Author: leavesCZY
 * @Date: 2021/12/7 11:07
 * @Desc:
 */
val ClassNode.simpleClassName: String
    get() = name.substringAfterLast('/')

val MethodNode.nameWithDesc: String
    get() = name + desc

val MethodNode.isStatic: Boolean
    get() = access and Opcodes.ACC_STATIC != 0

val MethodNode.isInitMethod: Boolean
    get() = name == "<init>"

fun MethodNode.hasAnnotation(annotationDesc: String): Boolean {
    return visibleAnnotations?.find { it.desc == annotationDesc } != null
}

fun MethodInsnNode.insertArgument(argumentType: Class<*>) {
    val type = Type.getMethodType(desc)
    val argumentTypes = type.argumentTypes
    val returnType = type.returnType
    val newArgumentTypes = arrayOfNulls<Type>(argumentTypes.size + 1)
    System.arraycopy(argumentTypes, 0, newArgumentTypes, 0, argumentTypes.size - 1 + 1)
    newArgumentTypes[newArgumentTypes.size - 1] = Type.getType(argumentType)
    desc = Type.getMethodDescriptor(returnType, *newArgumentTypes)
}

fun ClassNode.isHookPoint(config: DoubleClickConfig, methodNode: MethodNode): Boolean {
    val myInterfaces = interfaces
    if (myInterfaces.isNullOrEmpty()) {
        return false
    }
    val extraHookMethodList = config.hookPointList
    extraHookMethodList.forEach {
        if (myInterfaces.contains(it.interfaceName) && methodNode.nameWithDesc == it.methodSign) {
            return true
        }
    }
    return false
}

fun MethodNode.findHookPointLambda(config: DoubleClickConfig): List<InvokeDynamicInsnNode> {
    val onClickListenerLambda = findLambda {
        val nodeName = it.name
        val nodeDesc = it.desc
        val find = config.hookPointList.find { point ->
            nodeName == point.methodName && nodeDesc.endsWith(point.interfaceSignSuffix)
        }
        return@findLambda find != null
    }
    return onClickListenerLambda
}

private fun MethodNode.findLambda(
    filter: (InvokeDynamicInsnNode) -> Boolean
): List<InvokeDynamicInsnNode> {
    val handleList = mutableListOf<InvokeDynamicInsnNode>()
    val instructions = instructions?.iterator() ?: return handleList
    while (instructions.hasNext()) {
        val nextInstruction = instructions.next()
        if (nextInstruction is InvokeDynamicInsnNode) {
            if (filter(nextInstruction)) {
                handleList.add(nextInstruction)
            }
        }
    }
    return handleList
}