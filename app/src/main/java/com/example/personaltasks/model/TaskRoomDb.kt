package com.example.personaltasks.model

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Task::class], version = 1)
@TypeConverters(LocalDateConverter::class)
abstract class TaskRoomDb: RoomDatabase() {
    abstract fun taskDao(): TaskDAO
}