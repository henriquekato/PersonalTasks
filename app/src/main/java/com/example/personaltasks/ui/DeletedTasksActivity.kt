package com.example.personaltasks.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.personaltasks.adapter.DeletedTasksRvAdapter
import com.example.personaltasks.controllers.DeletedTasksController
import com.example.personaltasks.databinding.ActivityDeletedTasksBinding
import com.example.personaltasks.model.Task
import com.example.personaltasks.ui.Extras.EXTRA_TASK
import com.example.personaltasks.ui.Extras.EXTRA_TASK_ARRAY
import com.example.personaltasks.ui.Extras.EXTRA_VIEW_TASK

class DeletedTasksActivity : AppCompatActivity(), OnDeletedTaskClickListener{
    private val adtb: ActivityDeletedTasksBinding by lazy {
        ActivityDeletedTasksBinding.inflate(layoutInflater)
    }

    private val taskList: MutableList<Task> = mutableListOf()

    private val deletedTaskAdapter: DeletedTasksRvAdapter by lazy {
        DeletedTasksRvAdapter(taskList, this)
    }

    private val taskController: DeletedTasksController by lazy {
        DeletedTasksController(this)
    }

    companion object {
        const val GET_TASKS_MESSAGE = 1
        const val GET_TASKS_INTERVAL = 2000L
    }

    val getTasksHandler = object: Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == GET_TASKS_MESSAGE){
                taskController.retrieveDeletedTasks()
                sendMessageDelayed(obtainMessage().apply { what = GET_TASKS_MESSAGE }, GET_TASKS_INTERVAL)
            }
            else {
                val taskArray = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    msg.data?.getParcelableArray(EXTRA_TASK_ARRAY, Task::class.java)
                }
                else {
                    msg.data?.getParcelableArray(EXTRA_TASK_ARRAY)
                }
                taskList.clear()
                taskArray?.forEach { taskList.add(it as Task) }
                deletedTaskAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(adtb.root)

        setSupportActionBar(adtb.toolbarIn.toolbar)
        supportActionBar?.subtitle = "Deleted tasks"

        adtb.deletedTasksRv.adapter = deletedTaskAdapter
        adtb.deletedTasksRv.layoutManager = LinearLayoutManager(this)

        getTasksHandler.sendMessageDelayed(Message().apply { what = GET_TASKS_MESSAGE}, GET_TASKS_INTERVAL)
    }

    override fun onViewTask(position: Int) {
        Intent(this, TaskActivity::class.java).apply {
            putExtra(EXTRA_TASK, taskList[position])
            putExtra(EXTRA_VIEW_TASK, true)
            startActivity(this)
        }
    }

    override fun onRestoreTask(position: Int) {
        taskController.restoreTask(taskList[position])
        taskList.removeAt(position)
        deletedTaskAdapter.notifyItemRemoved(position)
    }
}