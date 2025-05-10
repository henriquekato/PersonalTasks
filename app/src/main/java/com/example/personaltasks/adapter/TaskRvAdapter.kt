package com.example.personaltasks.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.personaltasks.databinding.TileTaskBinding
import com.example.personaltasks.model.Task

class TaskRvAdapter(
    private val taskList: MutableList<Task>
) : RecyclerView.Adapter<TaskRvAdapter.TaskViewHolder>() {
    inner class TaskViewHolder(tcb: TileTaskBinding) : RecyclerView.ViewHolder(tcb.root) {
        val titleTv: TextView = tcb.titleTv
        val descriptionTv: TextView = tcb.descriptionTv
        val dueDateTv: TextView = tcb.dueDateTv
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
                dueDateTv.text = task.dueDate.toString()
            }
        }
    }

    override fun getItemCount(): Int = taskList.size

}