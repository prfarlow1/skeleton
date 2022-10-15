package com.peterfarlow

import android.os.Bundle
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.TextView
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CarBrainTheme {
                Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
                    Text(
                        text = "Hello, world!",
                        style = LocalTextStyle.current
                    )
                }

            }
        }
    }
}
