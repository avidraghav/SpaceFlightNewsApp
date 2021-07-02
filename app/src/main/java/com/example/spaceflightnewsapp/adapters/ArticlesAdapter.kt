package com.example.spaceflightnewsapp.adapters

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spaceflightnewsapp.R
import com.example.spaceflightnewsapp.databinding.ItemArticlePreviewBinding
import com.example.spaceflightnewsapp.models.ArticlesResponseItem
import com.example.spaceflightnewsapp.ui.MainActivity
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.util.*


class ArticlesAdapter  : RecyclerView.Adapter<ArticlesAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ItemArticlePreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article: ArticlesResponseItem = differ.currentList[position]
        holder.bind(article)

    }

    override fun getItemCount() = differ.currentList.size


    class ViewHolder(private val binding: ItemArticlePreviewBinding) : RecyclerView.ViewHolder(binding.root) {
        companion object{ var onItemClickListener : ((ArticlesResponseItem) -> Unit)? = null}

        fun bind(article: ArticlesResponseItem) {
            binding.apply {
                Glide.with(root)
                    .load(article.imageUrl)
                    .into(ivArticleImage)

                tvSource.text = article.newsSite
                tvTitle.text = article.title
                tvDescription.text = article.summary
                val date = article.publishedAt.subSequence(0,11).subSequence(8,10).toString()
                val month = article.publishedAt.subSequence(0,11).subSequence(5,7).toString()
                val year = article.publishedAt.subSequence(0,11).subSequence(0,4).toString()
                tvPublishedAt.text = date+"/"+month+"/"+year
                itemView.setOnClickListener {
                    onItemClickListener?.let { it(article) }
                }
            }
        }
    }


    fun setOnItemClickListener(listener: (ArticlesResponseItem) -> Unit) {
        ViewHolder.onItemClickListener = listener
    }
    private val differCallBack = object : DiffUtil.ItemCallback<ArticlesResponseItem>(){
        override fun areItemsTheSame(
            oldItem: ArticlesResponseItem, newItem: ArticlesResponseItem
        ): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(
            oldItem: ArticlesResponseItem, newItem: ArticlesResponseItem
        ): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this,differCallBack)
}