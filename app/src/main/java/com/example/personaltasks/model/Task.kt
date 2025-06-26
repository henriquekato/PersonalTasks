package com.example.personaltasks.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.personaltasks.model.Constants.INVALID_TASK_ID
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Task(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = INVALID_TASK_ID,
    var title: String = "",
    var description: String = "",
    var dueDate: String = "",
    var isDone: Boolean = false,
    var isDeleted: Boolean = false
): Parcelable
