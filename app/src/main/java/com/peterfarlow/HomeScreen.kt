package com.peterfarlow

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import java.util.*

@Composable
fun HomeScreen(navController: NavController) {
    SkeletonTheme {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
            Text(
                text = "Hello, world!",
                modifier = Modifier.clickable {
                    navController.navigate("login")
                },
                style = LocalTextStyle.current
            )
        }
    }
}

data class HomeScreenState(
    val loggedIn: Boolean = false,
    val reminders: List<Reminder> = emptyList(),
)

@Entity(tableName = "reminders")
data class Reminder(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val title: String = "",
    val body: String = "",
    val time: Long = Instant.now().toEpochMilli(),
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Reminder

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
