package com.example.personaltasks.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.personaltasks.databinding.ActivityTaskBinding
import com.example.personaltasks.ui.Extras.EXTRA_TASK
import com.example.personaltasks.ui.Extras.EXTRA_VIEW_TASK
import com.example.personaltasks.model.Task
import java.time.LocalDate

class TaskActivity : AppCompatActivity() {
    private val atb: ActivityTaskBinding by lazy {
        ActivityTaskBinding.inflate(layoutInflater)
    }

    private var selectedPriority = "Alta"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(atb.root)

        setSupportActionBar(atb.toolbarIn.toolbar)
        supportActionBar?.subtitle = "New task"

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        val task = getTaskFromIntent()
        task?.let {
            supportActionBar?.subtitle = "Edit task"
            with(atb) {
                setFormFieldsWithTaskValues(it)

                if (isViewMode()) {
                    supportActionBar?.subtitle = "View task"
                    activateViewMode()
                }
            }
        }

        with(atb) {
            saveBt.setOnClickListener {
                Task(
                    task?.id ?: hashCode(),
                    titleEt.text.toString(),
                    descriptionEt.text.toString(),
                    LocalDate.of(dueDateDp.year, dueDateDp.month, dueDateDp.dayOfMonth).toString(),
                    isDoneCb.isChecked,
                    false,
                    selectedPriority
                ).let { task ->
                    Intent().apply {
                        putExtra(EXTRA_TASK, task)
                        setResult(RESULT_OK, this)
                    }
                }
                finish()
            }
        }

        atb.cancelBt.setOnClickListener {
            finish()
        }

        with(atb){
            highPriorityRb.setOnClickListener {
                mediumPriorityRb.isChecked = false
                lowPriorityRb.isChecked = false
                selectedPriority = "Alta"
            }
            mediumPriorityRb.setOnClickListener {
                highPriorityRb.isChecked = false
                lowPriorityRb.isChecked = false
                selectedPriority = "Média"
            }
            lowPriorityRb.setOnClickListener {
                highPriorityRb.isChecked = false
                mediumPriorityRb.isChecked = false
                selectedPriority = "Baixa"
            }
        }
    }

    private fun getTaskFromIntent() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        intent.getParcelableExtra(EXTRA_TASK, Task::class.java)
    } else {
        intent.getParcelableExtra(EXTRA_TASK)
    }

    private fun ActivityTaskBinding.setFormFieldsWithTaskValues(task: Task) {
        titleEt.setText(task.title)
        descriptionEt.setText(task.description)
        val localDate = LocalDate.parse(task.dueDate)
        dueDateDp.updateDate(
            localDate.year,
            localDate.monthValue - 1,
            localDate.dayOfMonth
        )
        isDoneCb.isChecked = task.isDone
        when(task.priority){
            "Alta" -> {
                highPriorityRb.isChecked = true
                mediumPriorityRb.isChecked = false
                lowPriorityRb.isChecked = false
            }
            "Média" -> {
                mediumPriorityRb.isChecked = true
                highPriorityRb.isChecked = false
                lowPriorityRb.isChecked = false
            }
            "Baixa" -> {
                lowPriorityRb.isChecked = true
                highPriorityRb.isChecked = false
                mediumPriorityRb.isChecked = false
            }
        }
        selectedPriority = task.priority
    }

    private fun isViewMode() = intent.getBooleanExtra(EXTRA_VIEW_TASK, false)

    private fun ActivityTaskBinding.activateViewMode() {
        titleEt.isEnabled = false
        descriptionEt.isEnabled = false
        dueDateDp.isEnabled = false
        saveBt.visibility = View.GONE
        isDoneCb.isEnabled = false
        highPriorityRb.isEnabled = false
        mediumPriorityRb.isEnabled = false
        lowPriorityRb.isEnabled = false
    }
}