package com.raghav.spacedawn.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.raghav.spacedawn.R
import com.raghav.spacedawn.databinding.ItemLaunchPreviewBinding
import com.raghav.spacedawn.models.launchlibrary.LaunchLibraryResponseItem
import com.raghav.spacedawn.utils.Constants.Companion.DATE_OUTPUT_FORMAT
import com.raghav.spacedawn.utils.Constants.Companion.LAUNCH_DATE_INPUT_FORMAT
import com.raghav.spacedawn.utils.Helpers.Companion.formatTo
import com.raghav.spacedawn.utils.Helpers.Companion.toDate


class LaunchesAdapter()  : RecyclerView.Adapter<LaunchesAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ItemLaunchPreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val launch: LaunchLibraryResponseItem = differ.currentList[position]
        holder.bind(launch)
    }

    override fun getItemCount() = differ.currentList.size

    class ViewHolder(private val binding: ItemLaunchPreviewBinding) : RecyclerView.ViewHolder(binding.root) {
        companion object{ var onItemClickListener : ((LaunchLibraryResponseItem) -> Unit)? = null}
        fun bind(launch: LaunchLibraryResponseItem) {

            binding.apply {
                Glide.with(root)
                    .load(launch.image)
                    .placeholder(R.drawable.icon)
                    .error(R.drawable.icon)
                    .circleCrop()
                    .into(ivLaunchImage)

                tvAgency.text = launch.launch_service_provider.name
                tvTitle.text = launch.name
                tvRocketName.text = launch.rocket.configuration.full_name
                binding.tvStatus.setTextColor(
                    when(launch.status.name){
                        "To Be Determined" -> Color.RED
                        "Go for Launch" -> Color.GREEN
                        "To Be Confirmed" -> Color.YELLOW
                        else -> Color.WHITE
                    }
                )

                tvStatus.text = launch.status.name
                val dateTime= launch.net.toDate(LAUNCH_DATE_INPUT_FORMAT)

                tvLaunchDate.text =launch.net
                    .toDate(LAUNCH_DATE_INPUT_FORMAT)
                    .formatTo(
                    DATE_OUTPUT_FORMAT)


                btnSetAlarm.setOnClickListener {
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