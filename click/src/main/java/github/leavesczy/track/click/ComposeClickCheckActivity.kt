package github.leavesczy.track.click

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import github.leavesczy.track.click.compose.TransformTheme
import github.leavesczy.track.click.compose.clickableCheck
import github.leavesczy.track.click.compose.combinedClickableCheck

/**
 * @Author: leavesCZY
 * @Github: https://github.com/leavesCZY
 * @Desc:
 */
class ComposeClickCheckActivity : AppCompatActivity() {

    companion object {

        const val whiteListTag = "noCheck"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "Jetpack Compose 双击防抖"
        setContent {
            TransformTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    contentWindowInsets = WindowInsets.navigationBars
                ) { innerPadding ->
                    var index by remember {
                        mutableStateOf(0)
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues = innerPadding)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(all = 10.dp),
                                text = "index: $index",
                                fontSize = 22.sp
                            )
                            Text(
                                modifier = Modifier
                                    .clickable(onClickLabel = whiteListTag) {
                                        index++
                                    }
                                    .padding(all = 15.dp),
                                text = "Text clickable 不防抖"
                            )
                            Text(
                                modifier = Modifier
                                    .combinedClickable(
                                        onClickLabel = whiteListTag,
                                        onClick = {
                                            index++
                                        }
                                    )
                                    .padding(all = 15.dp),
                                text = "Text combinedClickable 不防抖"
                            )
                            Text(
                                modifier = Modifier
                                    .clickable {
                                        index++
                                    }
                                    .padding(all = 15.dp),
                                text = "Text clickable ASM 防抖"
                            )
                            Text(
                                modifier = Modifier
                                    .combinedClickable(
                                        onClick = {
                                            index++
                                        }
                                    )
                                    .padding(all = 15.dp),
                                text = "Text combinedClickable ASM 防抖"
                            )
                            Button(onClick = {
                                index++
                            }) {
                                Text(
                                    modifier = Modifier,
                                    text = "Button ASM 防抖"
                                )
                            }
                            TextButton(onClick = {
                                index++
                            }) {
                                Text(
                                    modifier = Modifier
                                        .padding(all = 15.dp),
                                    text = "TextButton ASM 防抖"
                                )
                            }
                            Text(
                                modifier = Modifier
                                    .clickableCheck {
                                        index++
                                    }
                                    .padding(all = 15.dp),
                                text = "Text clickable 自定义防抖"
                            )
                            Text(
                                modifier = Modifier
                                    .combinedClickableCheck(
                                        onClick = {
                                            index++
                                        }
                                    )
                                    .padding(all = 15.dp),
                                text = "Text combinedClickable 自定义防抖"
                            )
                        }
                    }
                }
            }
        }
    }

}