package github.leavesczy.asm.privacy.privacy

import android.app.Application

object ContextInject {

    internal lateinit var context: Application

    fun inject(context: Application) {
        this.context = context
    }

}