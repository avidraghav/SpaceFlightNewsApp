package com.example.spaceflightnewsapp.adapters

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.spaceflightnewsapp.databinding.ItemReminderPreviewBinding
import com.example.spaceflightnewsapp.db.ReminderDatabase
import com.example.spaceflightnewsapp.db.ReminderModelClass
import com.example.spaceflightnewsapp.models.launchlibrary.LaunchLibraryResponseItem
import com.example.spaceflightnewsapp.ui.MainActivity
import com.example.spaceflightnewsapp.utils.AlarmBroadCastReciever
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


class RemindersListAdapter() : RecyclerView.Adapter<RemindersListAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ItemReminderPreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)

    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val reminder: ReminderModelClass = differ.currentList[position]
        holder.bind(reminder)
    }
    override fun getItemCount() = differ.currentList.size

    class ViewHolder(private val binding: ItemReminderPreviewBinding) : RecyclerView.ViewHolder(binding.root) {
        companion object{ var onItemClickListener : ((ReminderModelClass) -> Unit)? = null}
        fun bind(reminder: ReminderModelClass) {
            binding.apply {
                tvTitle.text = reminder.name
                tvLaunchDate.text = reminder.dateTime
                btnCancelAlarm.setOnClickListener {
                    onItemClickListener?.let { it(reminder) }

                }
            }
        }

    }
    fun setOnItemClickListener(listener: (ReminderModelClass) -> Unit) {
        ViewHolder.onItemClickListener = listener
    }

    private val differCallBack = object : DiffUtil.ItemCallback<ReminderModelClass>(){
        override fun areItemsTheSame(
            oldItem: ReminderModelClass, newItem: ReminderModelClass
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ReminderModelClass, newItem: ReminderModelClass
        ): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this,differCallBack)
}