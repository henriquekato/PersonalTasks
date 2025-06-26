package com.example.personaltasks.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.personaltasks.R
import com.example.personaltasks.adapter.TaskRvAdapter
import com.example.personaltasks.controllers.TaskController
import com.example.personaltasks.databinding.ActivityMainBinding
import com.example.personaltasks.ui.Extras.EXTRA_TASK
import com.example.personaltasks.ui.Extras.EXTRA_VIEW_TASK
import com.example.personaltasks.model.Task
import com.example.personaltasks.ui.Extras.EXTRA_TASK_ARRAY
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(), OnTaskClickListener {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val taskList: MutableList<Task> = mutableListOf()

    private val taskAdapter: TaskRvAdapter by lazy {
        TaskRvAdapter(taskList, this)
    }

    private val taskController: TaskController by lazy {
        TaskController(this)
    }

    private lateinit var createTaskArl: ActivityResultLauncher<Intent>

    companion object {
        const val GET_TASKS_MESSAGE = 1
        const val GET_CONTACTS_INTERVAL = 2000L
    }

    val getTasksHandler = object: Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == GET_TASKS_MESSAGE){
                taskController.retrieveTasks()
                sendMessageDelayed(obtainMessage().apply { what = GET_TASKS_MESSAGE }, GET_CONTACTS_INTERVAL)
            }
            else {
                val contactArray = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    msg.data?.getParcelableArray(EXTRA_TASK_ARRAY, Task::class.java)
                }
                else {
                    msg.data?.getParcelableArray(EXTRA_TASK_ARRAY)
                }
                taskList.clear()
                contactArray?.forEach { taskList.add(it as Task) }
                taskAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        setSupportActionBar(amb.toolbarIn.toolbar)

        createTaskArl =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val task = getTaskFromIntent(result)
                    task?.let { receivedTask ->
                        val position = taskList.indexOfFirst { it.id == task.id }
                        if (isInList(position)) {
                            updateTask(receivedTask, position)
                        } else {
                            createTask(receivedTask)
                        }
                    }
                }
            }

        amb.taskRv.adapter = taskAdapter
        amb.taskRv.layoutManager = LinearLayoutManager(this)

        getTasksHandler.sendMessageDelayed(Message().apply { what = GET_TASKS_MESSAGE }, GET_CONTACTS_INTERVAL)
    }

    private fun getTaskFromIntent(result: ActivityResult) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            result.data?.getParcelableExtra(EXTRA_TASK, Task::class.java)
        } else {
            result.data?.getParcelableExtra(EXTRA_TASK)
        }

    private fun isInList(position: Int): Boolean{
        return position != -1
    }

    private fun createTask(receivedTask: Task) {
        taskController.createTask(receivedTask)
        taskList.add(receivedTask)
        taskAdapter.notifyItemInserted(taskList.lastIndex)
    }

    private fun updateTask(receivedTask: Task, position: Int) {
        taskController.updateTask(receivedTask)
        taskList[position] = receivedTask
        taskAdapter.notifyItemChanged(position)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_task_mi -> {
                createTaskArl.launch(Intent(this, TaskActivity::class.java))
                true
            }
            R.id.sign_out_mi -> {
                Firebase.auth.signOut()
                finish()
                true
            }
            else -> {
                false
            }
        }
    }

    override fun onViewTask(position: Int) {
        Intent(this, TaskActivity::class.java).apply {
            putExtra(EXTRA_TASK, taskList[position])
            putExtra(EXTRA_VIEW_TASK, true)
            startActivity(this)
        }
    }

    override fun onEditTask(position: Int) {
        Intent(this, TaskActivity::class.java).apply {
            putExtra(EXTRA_TASK, taskList[position])
            createTaskArl.launch(this)
        }
    }

    override fun onRemoveTask(position: Int) {
        taskController.deleteTask(taskList[position])
        taskList.removeAt(position)
        taskAdapter.notifyItemRemoved(position)
    }
}