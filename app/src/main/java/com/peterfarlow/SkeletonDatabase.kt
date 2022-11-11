package com.peterfarlow

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Database(
    entities = [Reminder::class],
    version = 1,
)
abstract class SkeletonDatabase : RoomDatabase() {

    abstract fun remindersDao(): RemindersDao
}

@Dao
interface RemindersDao {

    @Query("SELECT * FROM reminders")
    fun getAll(): Flow<List<Reminder>>

    @Insert
    suspend fun put(reminder: Reminder)

    @Delete()
    suspend fun delete(reminder: Reminder)
}
