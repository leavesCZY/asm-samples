package github.leavesc.asm

import android.app.Application

/**
 * @Author: leavesC
 * @Date: 2021/12/25 15:28
 * @Desc:
 * @Github：https://github.com/leavesC
 */
class MainApplication : Application() {

    companion object {
        lateinit var application: Application
            private set
    }

    override fun onCreate() {
        super.onCreate()
        application = this
    }

}