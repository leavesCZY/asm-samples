package github.leavesczy.asm.doubleClick.compose

import android.util.Log

/**
 * @Author: leavesCZY
 * @Date: 2022/10/15 20:09
 * @Desc:
 * @Github：https://github.com/leavesCZY
 */
class OnClickWrap(private val onClick: (() -> Unit)) : Function0<Unit> {

    companion object {

        private const val MIN_DURATION = 500L

        private var lastClickTime = 0L

    }

    override fun invoke() {
        val currentTime = System.currentTimeMillis()
        if (lastClickTime == 0L || currentTime - lastClickTime > MIN_DURATION) {
            lastClickTime = currentTime
            onClick()
            log("onClick isEnabled : true")
        } else {
            log("onClick isEnabled : false")
        }
    }

    private fun log(log: String) {
        Log.e("ViewDoubleClickCheck", log)
    }

}