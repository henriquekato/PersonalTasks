package com.example.personaltasks.ui

sealed interface OnTaskClickListener {
    fun onViewTask(position: Int)
    fun onEditTask(position: Int)
    fun onRemoveTask(position: Int)
}