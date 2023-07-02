package github.leavesczy.asm

import android.app.Application
import github.leavesczy.asm.privacy.privacy.ContextInject

/**
 * @Author: leavesCZY
 * @Date: 2021/12/25 15:28
 * @Desc:
 * @Github：https://github.com/leavesCZY
 */
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ContextInject.inject(context = this)
    }

}