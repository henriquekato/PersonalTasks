package com.example.personaltasks.controllers

import androidx.room.Room
import com.example.personaltasks.model.Task
import com.example.personaltasks.model.TaskDAO
import com.example.personaltasks.model.TaskRoomDb
import com.example.personaltasks.ui.MainActivity

class TaskController(mainActivity: MainActivity) {
    private val taskDao: TaskDAO = Room.databaseBuilder(
        mainActivity,
        TaskRoomDb::class.java,
        "task-database"
    ).build().taskDao()

    fun createTask(task: Task) {
        Thread {
            taskDao.createTask(task)
        }.start()
    }
    fun retrieveTask(id: Int) = taskDao.retrieveTask(id)
    fun retrieveTasks() = taskDao.retrieveTasks()
    fun updateTask(task: Task) {
        Thread {
            taskDao.updateTask(task)
        }.start()
    }
    fun deleteTask(task: Task) {
        Thread {
            taskDao.deleteTask(task)
        }.start()
    }
}