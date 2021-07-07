package com.example.spaceflightnewsapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spaceflightnewsapp.R
import com.example.spaceflightnewsapp.databinding.ItemArticlePreviewBinding
import com.example.spaceflightnewsapp.databinding.ItemReminderPreviewBinding
import com.example.spaceflightnewsapp.db.ReminderModelClass
import com.example.spaceflightnewsapp.models.spaceflightapi.ArticlesResponseItem
import com.example.spaceflightnewsapp.utils.Constants
import com.example.spaceflightnewsapp.utils.Constants.Companion.ARTICLE_DATE_INPUT_FORMAT
import com.example.spaceflightnewsapp.utils.Constants.Companion.DATE_OUTPUT_FORMAT
import com.example.spaceflightnewsapp.utils.Helpers.Companion.formatTo
import com.example.spaceflightnewsapp.utils.Helpers.Companion.toDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class RemindersListAdapter  : RecyclerView.Adapter<RemindersListAdapter.ViewHolder>(){

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
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(
            oldItem: ReminderModelClass, newItem: ReminderModelClass
        ): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this,differCallBack)
}