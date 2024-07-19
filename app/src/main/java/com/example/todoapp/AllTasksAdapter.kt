package com.example.todoapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.todoapp.databinding.LayoutCurrentTasksBinding

interface TaskActionListener {
    fun onEditClick(task: Task)
    fun onDeleteClick(task: Task)
    fun onCompleteClick(task: Task)
}

class AllTasksAdapter(private val listener: TaskActionListener): RecyclerView.Adapter<AllTasksAdapter.TaskViewHolder>() {
    private var tasks: List<Task> = emptyList()
    class TaskViewHolder(itemView:View):ViewHolder(itemView) {
        val bind = LayoutCurrentTasksBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_current_tasks, parent, false)
        return TaskViewHolder(view)
    }


    override fun getItemCount(): Int {
        return tasks.size
    }

    fun setTasks(tasks: List<Task>) {
        this.tasks = tasks
        notifyDataSetChanged()
    }

    private val taskDiffCallback = object:DiffUtil.ItemCallback<Task>(){
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }

    }

    val taskDifferList = AsyncListDiffer(this, taskDiffCallback)


    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.bind.taskTitleTv.text = task.taskTitle
        holder.bind.taskSubtitleTv.text = task.taskSubtitle

        holder.bind.apply {
            if(task.isCompleted){
                holder.bind.editBtn.visibility = View.GONE
                holder.bind.completeBtn.visibility = View.GONE
                holder.bind.taskCard.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.completed_task_card))
            }
        }

        holder.bind.editBtn.setOnClickListener {
            listener.onEditClick(task)
        }
        holder.bind.deleteBtn.setOnClickListener {
            listener.onDeleteClick(task)
        }

        holder.bind.completeBtn.setOnClickListener {
            listener.onCompleteClick(task)
        }
    }
}