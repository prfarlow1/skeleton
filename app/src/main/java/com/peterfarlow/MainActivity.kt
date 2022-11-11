package com.peterfarlow

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.tinylog.kotlin.Logger

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.error { "hello!" }
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "home") {
                composable("home") {
                    HomeScreen()
                }
                composable("login") {

                }
                composable("create-account") {

                }
                composable("onboarding") {

                }
            }
        }
    }
}
