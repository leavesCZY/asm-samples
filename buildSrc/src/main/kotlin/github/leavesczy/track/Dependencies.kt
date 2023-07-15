/**
 * @Author: leavesCZY
 * @Github: https://github.com/leavesCZY
 * @Desc:
 */
object Dependencies {

    object Test {
        const val junit = "junit:junit:4.13.2"
        const val testExt = "androidx.test.ext:junit:1.1.5"
        const val espresso = "androidx.test.espresso:espresso-core:3.5.1"
    }

    object Components {
        const val appcompat = "androidx.appcompat:appcompat:1.6.1"
        const val material = "com.google.android.material:material:1.9.0"
        const val baseRecyclerView = "io.github.cymchad:BaseRecyclerViewAdapterHelper:3.0.14"
        const val commonsIO = "commons-io:commons-io:2.13.0"
    }

    object Compose {
        const val compilerVersion = "1.4.8"
        const val bom = "androidx.compose:compose-bom:2023.06.01"
        const val ui = "androidx.compose.ui:ui"
        const val material3 = "androidx.compose.material3:material3"
        const val activity = "androidx.activity:activity-compose:1.7.2"
    }

}