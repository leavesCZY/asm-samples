package github.leavesczy.trace.privacy

import android.app.Application

/**
 * @Author: leavesCZY
 * @Github: https://github.com/leavesCZY
 * @Desc:
 */
object ContextProvider {

    internal lateinit var context: Application

    fun inject(context: Application) {
        this.context = context
    }

}