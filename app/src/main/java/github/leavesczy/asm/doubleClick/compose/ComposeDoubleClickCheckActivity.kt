package github.leavesczy.asm.doubleClick.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import github.leavesczy.asm.R
import github.leavesczy.asm.doubleClick.compose.theme.TransformTheme

/**
 * @Author: CZY
 * @Date: 2022/10/9 15:49
 * @Desc:
 */
class ComposeDoubleClickCheckActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TransformTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    contentWindowInsets = WindowInsets.navigationBars,
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = "Jetpack Compose 双击防抖",
                                    fontSize = 20.sp,
                                    color = Color.White
                                )
                            },
                            colors = TopAppBarDefaults.smallTopAppBarColors(
                                containerColor = colorResource(
                                    id = R.color.purple_700
                                )
                            )
                        )
                    }
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
                                    .padding(all = 40.dp),
                                text = "index: $index"
                            )
                            Text(
                                modifier = Modifier
                                    .clickable {
                                        index++
                                    }
                                    .padding(all = 20.dp),
                                text = "Text clickable"
                            )
                            Text(
                                modifier = Modifier
                                    .combinedClickable(
                                        onClick = {
                                            index++
                                        }
                                    )
                                    .padding(all = 20.dp),
                                text = "Text combinedClickable"
                            )
                            Button(onClick = {
                                index++
                            }) {
                                Text(
                                    modifier = Modifier,
                                    text = "Button"
                                )
                            }
                            TextButton(onClick = {
                                index++
                            }) {
                                Text(
                                    modifier = Modifier
                                        .padding(all = 20.dp),
                                    text = "TextButton"
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}