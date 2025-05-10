package com.example.personaltasks.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class Task(
    var id: Int,
    var title: String,
    var description: String,
    var dueDate: LocalDate
): Parcelable {

}