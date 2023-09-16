package com.peterfarlow

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.activity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.tinylog.kotlin.Logger

@OptIn(ExperimentalMaterialApi::class, ExperimentalLayoutApi::class,
    ExperimentalComposeUiApi::class
)
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.error { "hello!" }
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "test") {
                composable("home") {
                    HomeScreen(navController)
                }
                activity("login") {
                    activityClass = LoginActivity::class
                }
                composable("test") {
                    val keyboardInset = rememberUpdatedState(WindowInsets.ime)
                    //val navBarInset = rememberUpdatedState(WindowInsets.navigationBars)
                    val keyboardBottom = keyboardInset.value.getBottom(LocalDensity.current)
                    val focusManager = LocalFocusManager.current
                    /*val paddingBottom = with(LocalDensity.current) {
                        if (WindowInsets.isImeVisible) {
                            maxOf(
                                keyboardBottom,
                                navBarInset.value.getBottom(this)
                            ).toDp()
                        } else {
                            navBarInset.value.getBottom(this).toDp()
                        }
                    }*/
                    println("WindowInsets.isImeVisible=${WindowInsets.isImeVisible} keyboardBottom=$keyboardBottom")
                    ModalBottomSheetLayout(
                        modifier = Modifier
                            .fillMaxSize()
                            .statusBarsPadding()
                            .navigationBarsPadding(),//.imeNestedScroll(),
                        sheetContent = { }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                //.padding(bottom = paddingBottom)
                        ) {
                            val state = rememberLazyListState()
                            val scrollingUp = state.isScrollingUp()
                            val keyboardController = LocalSoftwareKeyboardController.current
                            LaunchedEffect(scrollingUp) {
                                if (scrollingUp) {
                                    keyboardController?.hide()
                                    focusManager.clearFocus()
                                }
                            }
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1F),
                                state = state,
                                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Bottom),
                            ) {
                                items(25) {
                                    Card {
                                        Text(
                                            text = loremIpsum
                                        )
                                    }
                                }
                            }
                            val shorter = remember { loremIpsum.split(" ").take(5) }.joinToString()
                            Row(
                                modifier = Modifier
                                    .horizontalScroll(rememberScrollState())
                                    .padding(start = 8.dp, end = 8.dp, bottom = 12.dp, top = 24.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                repeat(5) {
                                    Card(

                                    ) {
                                        Text(
                                            text = shorter
                                        )
                                    }
                                }
                            }
                            var text by remember { mutableStateOf(TextFieldValue("hello there")) }
                            BasicTextField(
                                value = text,
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                                keyboardActions = KeyboardActions(onSend = { println("send!"); text = text.copy(text = "") }),
                                onValueChange = { text = it },
                                modifier = Modifier
                                    .height(100.dp)
                                    .fillMaxWidth()
                            )
                        }
                    }
                }
                composable("onboarding") {

                }
            }
        }
    }
}

@Composable
private fun LazyListState.isScrollingUp(): Boolean {
    var previousIndex by remember(this) { mutableIntStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableIntStateOf(firstVisibleItemScrollOffset) }
    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousScrollOffset >= firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}

//val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "appData")

private val loremIpsum =
    "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.\n"
