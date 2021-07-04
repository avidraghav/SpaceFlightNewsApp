package com.example.spaceflightnewsapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spaceflightnewsapp.R
import com.example.spaceflightnewsapp.databinding.ItemArticlePreviewBinding
import com.example.spaceflightnewsapp.models.launchlibrary.LaunchLibraryResponseItem
import com.example.spaceflightnewsapp.utils.Constants.Companion.DATE_INPUT_FORMAT
import com.example.spaceflightnewsapp.utils.Constants.Companion.DATE_OUTPUT_FORMAT
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class LaunchesAdapter  : RecyclerView.Adapter<LaunchesAdapter.ViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ItemArticlePreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val launch: LaunchLibraryResponseItem = differ.currentList[position]
        holder.bind(launch)
    }

    override fun getItemCount() = differ.currentList.size


    class ViewHolder(private val binding: ItemArticlePreviewBinding) : RecyclerView.ViewHolder(binding.root) {
        companion object{ var onItemClickListener : ((LaunchLibraryResponseItem) -> Unit)? = null}

        fun bind(launch: LaunchLibraryResponseItem) {
            val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
            val outputFormatter = DateTimeFormatter.ofPattern(DATE_OUTPUT_FORMAT, Locale.ENGLISH)
            binding.apply {
                Glide.with(root)
                    .load(launch.image)
                    .placeholder(R.drawable.icon)
                    .error(R.drawable.icon)
                    .into(ivArticleImage)

                tvSource.text = launch.net
                tvTitle.text = launch.name
                tvDescription.text = launch.slug

                val date = LocalDateTime.parse(launch.net, inputFormatter)
               tvPublishedAt.text =  outputFormatter.format(date).toString()

                itemView.setOnClickListener {
                    onItemClickListener?.let { it(launch) }
                }
            }
        }
    }


    fun setOnItemClickListener(listener: (LaunchLibraryResponseItem) -> Unit) {
        ViewHolder.onItemClickListener = listener
    }
    private val differCallBack = object : DiffUtil.ItemCallback<LaunchLibraryResponseItem>(){
        override fun areItemsTheSame(
            oldItem: LaunchLibraryResponseItem, newItem: LaunchLibraryResponseItem
        ): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(
            oldItem: LaunchLibraryResponseItem, newItem: LaunchLibraryResponseItem
        ): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this,differCallBack)
}