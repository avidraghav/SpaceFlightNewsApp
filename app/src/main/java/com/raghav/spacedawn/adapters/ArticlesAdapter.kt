package com.raghav.spacedawn.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.raghav.spacedawn.R
import com.raghav.spacedawn.databinding.ItemArticlePreviewBinding
import com.raghav.spacedawn.models.spaceflightapi.ArticlesResponseItem
import com.raghav.spacedawn.utils.Constants.Companion.ARTICLE_DATE_INPUT_FORMAT
import com.raghav.spacedawn.utils.Constants.Companion.DATE_OUTPUT_FORMAT
import com.raghav.spacedawn.utils.Helpers.Companion.formatTo
import com.raghav.spacedawn.utils.Helpers.Companion.toDate


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
                    .placeholder(R.drawable.icon)
                    .error(R.drawable.icon)
                    .into(ivArticleImage)

                tvSource.text = article.newsSite
                tvTitle.text = article.title
                tvDescription.text = article.summary
                tvPublishedAt.text = article.publishedAt
                    .toDate(ARTICLE_DATE_INPUT_FORMAT)
                    .formatTo(DATE_OUTPUT_FORMAT)


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