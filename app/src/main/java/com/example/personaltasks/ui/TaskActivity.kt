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
                    LocalDate.of(dueDateDp.year, dueDateDp.month, dueDateDp.dayOfMonth),
                    isDoneCb.isChecked
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
    }

    private fun getTaskFromIntent() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        intent.getParcelableExtra(EXTRA_TASK, Task::class.java)
    } else {
        intent.getParcelableExtra(EXTRA_TASK)
    }

    private fun ActivityTaskBinding.setFormFieldsWithTaskValues(task: Task) {
        titleEt.setText(task.title)
        descriptionEt.setText(task.description)
        dueDateDp.updateDate(
            task.dueDate.year,
            task.dueDate.monthValue - 1,
            task.dueDate.dayOfMonth
        )
        isDoneCb.isChecked = task.isDone
    }

    private fun isViewMode() = intent.getBooleanExtra(EXTRA_VIEW_TASK, false)

    private fun ActivityTaskBinding.activateViewMode() {
        titleEt.isEnabled = false
        descriptionEt.isEnabled = false
        dueDateDp.isEnabled = false
        saveBt.visibility = View.GONE
        isDoneCb.isEnabled = false
    }
}