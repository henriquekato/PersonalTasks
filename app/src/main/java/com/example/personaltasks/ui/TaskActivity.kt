package com.example.personaltasks.ui

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.personaltasks.databinding.ActivityTaskBinding
import com.example.personaltasks.model.Constant.EXTRA_TASK
import com.example.personaltasks.model.Task

class TaskActivity : AppCompatActivity() {
    private val atb: ActivityTaskBinding by lazy {
        ActivityTaskBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(atb.root)

        setSupportActionBar(atb.toolbarIn.toolbar)

        val task = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_TASK, Task::class.java)
        } else {
            intent.getParcelableExtra(EXTRA_TASK)
        }
        task?.let{
            supportActionBar?.subtitle = "View task"
            with(atb){
                titleEt.setText(it.title)
                descriptionEt.setText(it.description)
                dueDateDp.updateDate(it.dueDate.year, it.dueDate.monthValue - 1, it.dueDate
                    .dayOfMonth)

                titleEt.isEnabled = false
                descriptionEt.isEnabled = false
                dueDateDp.isEnabled = false
                saveBt.visibility = View.GONE
            }
        }

        atb.cancelBt.setOnClickListener {
            finish()
        }
    }
}