package com.example.todoapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.todoapp.databinding.LayoutCurrentTasksBinding

class CompletedTaskAdapter: RecyclerView.Adapter<CompletedTaskAdapter.CompletedTaskViewHolder>(){

    private var completedTasks: List<Task> = listOf()

    class CompletedTaskViewHolder(itemView: View): ViewHolder(itemView){
        val bind = LayoutCurrentTasksBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompletedTaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_current_tasks, parent, false)
        return CompletedTaskViewHolder(view)
    }

    override fun getItemCount(): Int {
        return completedTasks.size
    }

    fun setCompletedTasks(completedTasks: List<Task>){
        this.completedTasks = completedTasks
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: CompletedTaskViewHolder, position: Int) {
        val task = completedTasks[position]
        holder.bind.taskTitleTv.text = task.taskTitle
        holder.bind.taskSubtitleTv.text = task.taskSubtitle

        holder.bind.apply {
            holder.bind.editBtn.visibility = View.GONE
            holder.bind.completeBtn.visibility = View.GONE
            holder.bind.taskCardLayout.background = ContextCompat.getDrawable(
                holder.itemView.context,
                R.drawable.background_completed_card
            )
        }
    }
}