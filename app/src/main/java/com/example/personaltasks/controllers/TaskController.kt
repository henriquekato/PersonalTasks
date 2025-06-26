package com.example.personaltasks.controllers

import android.os.Message
import com.example.personaltasks.model.Task
import com.example.personaltasks.model.TaskDAO
import com.example.personaltasks.model.TaskFirebaseDatabase
import com.example.personaltasks.ui.Extras.EXTRA_TASK_ARRAY
import com.example.personaltasks.ui.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskController(private val mainActivity: MainActivity) {
    private val taskDAO: TaskDAO = TaskFirebaseDatabase()
    private val databaseCoroutineScope = CoroutineScope(Dispatchers.IO)

    fun createTask(task: Task) {
        databaseCoroutineScope.launch {
            taskDAO.createTask(task)
        }
    }

    fun retrieveTasks(){
        databaseCoroutineScope.launch {
            val taskList = taskDAO.retrieveTasks().filter { !it.isDeleted }
            mainActivity.getTasksHandler.sendMessage(Message().apply {
                data.putParcelableArray(EXTRA_TASK_ARRAY, taskList.toTypedArray())
            })
        }
    }

    fun updateTask(task: Task) {
        databaseCoroutineScope.launch {
            taskDAO.updateTask(task)
        }
    }

    fun deleteTask(task: Task) {
        databaseCoroutineScope.launch {
            taskDAO.deleteTask(task)
        }
    }
}