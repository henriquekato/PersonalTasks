package com.example.personaltasks.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.personaltasks.R
import com.example.personaltasks.databinding.TileTaskBinding
import com.example.personaltasks.model.Task
import com.example.personaltasks.ui.OnDeletedTaskClickListener
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class DeletedTasksRvAdapter(
    private val taskList: MutableList<Task>,
    private val onDeletedTaskClickListener: OnDeletedTaskClickListener
) : RecyclerView.Adapter<DeletedTasksRvAdapter.TaskViewHolder>() {
    inner class TaskViewHolder(tcb: TileTaskBinding) : RecyclerView.ViewHolder(tcb.root) {
        val titleTv: TextView = tcb.titleTv
        val descriptionTv: TextView = tcb.descriptionTv
        val dueDateTv: TextView = tcb.dueDateTv
        val isDoneTv: TextView = tcb.isDoneTv

        init {
            tcb.root.setOnCreateContextMenuListener{ menu, v, menuInfo ->
                (onDeletedTaskClickListener as AppCompatActivity).menuInflater
                    .inflate(R.menu.context_menu_task, menu)

                menu.findItem(R.id.view_task_mi).setOnMenuItemClickListener {
                    onDeletedTaskClickListener.onViewTask(adapterPosition)
                    true
                }
                menu.findItem(R.id.restore_task_mi).setOnMenuItemClickListener {
                    onDeletedTaskClickListener.onRestoreTask(adapterPosition)
                    true
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TaskViewHolder =
        TaskViewHolder(
            TileTaskBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        taskList[position].let { task ->
            with(holder) {
                titleTv.text = task.title
                descriptionTv.text = task.description
                dueDateTv.text = task.dueDate
                    .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
                isDoneTv.text = "Not done"
                if(task.isDone) isDoneTv.text = "Done"
            }
        }
    }

    override fun getItemCount(): Int = taskList.size
}