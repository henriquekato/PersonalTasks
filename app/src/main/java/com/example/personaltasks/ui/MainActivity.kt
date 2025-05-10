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
import com.example.personaltasks.databinding.ActivityMainBinding
import com.example.personaltasks.model.Constant.EXTRA_TASK
import com.example.personaltasks.model.Constant.EXTRA_VIEW_TASK
import com.example.personaltasks.model.Task
import java.time.LocalDate

class MainActivity : AppCompatActivity(), OnTaskClickListener {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val taskList: MutableList<Task> = mutableListOf()

    private val taskAdapter: TaskRvAdapter by lazy {
        TaskRvAdapter(taskList, this)
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
                        result.data?.getParcelableExtra<Task>(EXTRA_TASK)
                    }
                    task?.let { receivedTask ->
                        taskList.add(receivedTask)
                        taskAdapter.notifyItemInserted(taskList.lastIndex)
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
        TODO("Not yet implemented")
    }

    override fun onRemoveTask(position: Int) {
        taskList.removeAt(position)
        taskAdapter.notifyItemRemoved(position)
    }

    private fun fillTaskList(){
        taskList.clear()
        for (i in 0..20){
            taskList.add(Task(i, "Task $i", "Description $i", LocalDate.now().plusDays(i.toLong())))
        }
        taskAdapter.notifyDataSetChanged()
    }
}