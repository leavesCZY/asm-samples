package github.leavesczy.asm.doubleClick.compose

import android.util.Log

/**
 * @Author: leavesCZY
 * @Date: 2022/10/15 20:09
 * @Desc:
 * @Github：https://github.com/leavesCZY
 */
class ClickableWrap(private val onClick: (() -> Unit)) : Function0<Unit> {

    companion object {

        private const val MIN_DURATION = 500L

        private var lastClickTime = 0L

    }

    override fun invoke() {
        Log.e("ClickableWrap", "invoke")
        val currentTime = System.currentTimeMillis()
        if (lastClickTime == 0L || currentTime - lastClickTime > MIN_DURATION) {
            lastClickTime = currentTime
            onClick()
        }
    }

}