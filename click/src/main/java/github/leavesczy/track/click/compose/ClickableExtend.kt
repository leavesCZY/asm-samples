package github.leavesczy.track.click.compose

import android.os.SystemClock
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.semantics.Role
import github.leavesczy.track.click.ComposeClickCheckActivity

/**
 * @Author: leavesCZY
 * @Github: https://github.com/leavesCZY
 * @Desc:
 */
private const val MIN_DURATION = 500L

internal inline fun Modifier.clickableCheck(
    minDuration: Long = MIN_DURATION,
    enabled: Boolean = true,
    role: Role? = null,
    crossinline onClick: () -> Unit
): Modifier = composed {
    var lastClickTime by remember {
        mutableStateOf(value = 0L)
    }
    clickable(
        enabled = enabled,
        onClickLabel = ComposeClickCheckActivity.whiteListTag,
        role = role
    ) {
        val currentTime = SystemClock.elapsedRealtime()
        if (currentTime - lastClickTime > minDuration) {
            lastClickTime = currentTime
            onClick()
        }
    }
}

internal fun Modifier.combinedClickableCheck(
    minDuration: Long = MIN_DURATION,
    enabled: Boolean = true,
    role: Role? = null,
    onLongClickLabel: String? = null,
    onLongClick: (() -> Unit)? = null,
    onDoubleClick: (() -> Unit)? = null,
    onClick: () -> Unit
): Modifier = composed {
    var lastClickTime by remember {
        mutableStateOf(value = 0L)
    }
    combinedClickable(
        enabled = enabled,
        onClickLabel = ComposeClickCheckActivity.whiteListTag,
        role = role,
        onLongClickLabel = onLongClickLabel,
        onLongClick = onLongClick,
        onDoubleClick = onDoubleClick
    ) {
        val currentTime = SystemClock.elapsedRealtime()
        if (currentTime - lastClickTime > minDuration) {
            lastClickTime = currentTime
            onClick()
        }
    }
}