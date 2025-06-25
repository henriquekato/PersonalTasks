package com.example.personaltasks.ui

interface OnDeletedTaskClickListener {
    fun onViewTask(position: Int)
    fun onRestoreTask(position: Int)
}