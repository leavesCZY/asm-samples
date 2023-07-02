package github.leavesczy.asm.click.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.semantics.Role
import github.leavesczy.asm.click.ComposeDoubleClickCheckActivity

/**
 * @Author: CZY
 * @Date: 2022/10/21 15:22
 * @Desc:
 */
const val MIN_DURATION = 500L

inline fun Modifier.clickableCheck(
    enabled: Boolean = true,
    role: Role? = null,
    crossinline onClick: () -> Unit
): Modifier = composed {
    var lastClickTime by remember { mutableStateOf(value = 0L) }
    clickable(
        enabled = enabled,
        onClickLabel = ComposeDoubleClickCheckActivity.whiteListTag,
        role = role
    ) {
        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis - lastClickTime > MIN_DURATION) {
            lastClickTime = currentTimeMillis
            onClick()
        }
    }
}

fun Modifier.combinedClickableCheck(
    enabled: Boolean = true,
    role: Role? = null,
    onLongClickLabel: String? = null,
    onLongClick: (() -> Unit)? = null,
    onDoubleClick: (() -> Unit)? = null,
    onClick: () -> Unit
): Modifier = composed {
    var lastClickTime by remember { mutableStateOf(value = 0L) }
    combinedClickable(
        enabled = enabled,
        onClickLabel = ComposeDoubleClickCheckActivity.whiteListTag,
        role = role,
        onLongClickLabel = onLongClickLabel,
        onLongClick = onLongClick,
        onDoubleClick = onDoubleClick
    ) {
        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis - lastClickTime > MIN_DURATION) {
            lastClickTime = currentTimeMillis
            onClick()
        }
    }
}