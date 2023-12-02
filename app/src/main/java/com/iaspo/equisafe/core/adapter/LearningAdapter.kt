package com.iaspo.equisafe.core.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iaspo.equisafe.core.data.network.response.learningresponse.VideosItem
import com.iaspo.equisafe.core.domain.model.LearningModel
import com.iaspo.equisafe.core.utils.withNotNull
import com.iaspo.equisafe.databinding.ItemListMaterialBinding
import com.iaspo.equisafe.ui.learning.DetailLearningActivity

class LearningAdapter : PagingDataAdapter<VideosItem, LearningAdapter.MyViewHolder>(DIFF_CALLBACK) {
    class MyViewHolder(private val binding: ItemListMaterialBinding): RecyclerView.ViewHolder(binding.root)  {
        fun bind(data: VideosItem){

            Glide.with(itemView.context)
                .load(data.thumbnailLink)
                .into(binding.imgMaterial)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailLearningActivity::class.java)

                val extraData = LearningModel(
                    id = data.id!!,
                    title = data.title!!,
                    description = data.description!!,
                    link = data.link!!,
                    thumbnailLink = data.thumbnailLink!!,
                    isFavorite = data.isFavorite!!
                )
                intent.putExtra(DetailLearningActivity.EXTRA_DATA, extraData)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        data.withNotNull {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val binding = ItemListMaterialBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<VideosItem>() {
            override fun areItemsTheSame(oldItem: VideosItem, newItem: VideosItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: VideosItem, newItem: VideosItem): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }
}