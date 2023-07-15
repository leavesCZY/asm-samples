package github.leavesczy.track

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import github.leavesczy.track.privacy.ContextProvider

/**
 * @Author: leavesCZY
 * @Github: https://github.com/leavesCZY
 * @Desc:
 */
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        ContextProvider.inject(context = this)
    }

}