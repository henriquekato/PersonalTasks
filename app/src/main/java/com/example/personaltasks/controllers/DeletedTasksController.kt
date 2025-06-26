package com.example.personaltasks.controllers

import android.os.Message
import com.example.personaltasks.model.Task
import com.example.personaltasks.model.TaskDAO
import com.example.personaltasks.model.TaskFirebaseDatabase
import com.example.personaltasks.ui.DeletedTasksActivity
import com.example.personaltasks.ui.Extras.EXTRA_TASK_ARRAY
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DeletedTasksController(private val deletedTasksActivity: DeletedTasksActivity) {
    private val taskDAO: TaskDAO = TaskFirebaseDatabase()
    private val databaseCoroutineScope = CoroutineScope(Dispatchers.IO)

    fun retrieveDeletedTasks(){
        databaseCoroutineScope.launch {
            val taskList = taskDAO.retrieveTasks().filter { it.isDeleted }
            deletedTasksActivity.getTasksHandler.sendMessage(Message().apply {
                data.putParcelableArray(EXTRA_TASK_ARRAY, taskList.toTypedArray())
            })
        }
    }

    fun restoreTask(task: Task): Int {
        task.isDeleted = false
        return taskDAO.updateTask(task)
    }
}