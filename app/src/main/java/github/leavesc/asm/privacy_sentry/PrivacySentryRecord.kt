package github.leavesc.asm.privacy_sentry

import github.leavesc.asm.MainApplication
import org.apache.commons.io.FileUtils
import java.io.File
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.*

object PrivacySentryRecord {

    private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS", Locale.CHINA)

    @JvmStatic
    fun writeToFile(log: String) {
        val logFile = File(MainApplication.application.externalCacheDir, "PrivacySentry.txt")
        val time = simpleDateFormat.format(Date(System.currentTimeMillis()))
        val logFormat = time + "\n" + log
        FileUtils.write(
            logFile,
            logFormat,
            Charset.defaultCharset(),
            true
        )
    }

}