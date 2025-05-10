package com.example.personaltasks.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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

    private lateinit var arl: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        setSupportActionBar(amb.toolbarIn.toolbar)

        arl =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val task = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        result.data?.getParcelableExtra(EXTRA_TASK, Task::class.java)
                    } else {
                        result.data?.getParcelableExtra(EXTRA_TASK)
                    }
                    task?.let { receivedTask ->
                        val position = taskList.indexOfFirst { it.id == receivedTask.id }

                        if (position == -1) {
                            taskController.createTask(receivedTask)
                            taskList.add(receivedTask)
                            taskAdapter.notifyItemInserted(taskList.lastIndex)
                        } else {
                            taskController.updateTask(receivedTask)
                            taskList[position] = receivedTask
                            taskAdapter.notifyItemChanged(position)
                        }
                    }
                }
            }

        amb.taskRv.adapter = taskAdapter
        amb.taskRv.layoutManager = LinearLayoutManager(this)

        fillTaskList()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_task_mi -> {
                arl.launch(Intent(this, TaskActivity::class.java))
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
            arl.launch(this)
        }
    }

    override fun onRemoveTask(position: Int) {
        taskController.deleteTask(taskList[position])
        taskList.removeAt(position)
        taskAdapter.notifyItemRemoved(position)
    }

    private fun fillTaskList() {
        taskList.clear()
        Thread {
            taskList.addAll(taskController.retrieveTasks())
            taskAdapter.notifyDataSetChanged()
        }.start()
    }
}