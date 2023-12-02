package com.iaspo.equisafe.core.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.iaspo.equisafe.core.data.network.response.homeresponse.FavoritesVideoItem
import com.iaspo.equisafe.core.domain.model.LearningModel
import com.iaspo.equisafe.databinding.ItemFavoritesBinding
import com.iaspo.equisafe.ui.home.article.DetailArticleActivity
import com.iaspo.equisafe.ui.learning.DetailLearningActivity

class FavoriteAdapter(private val listFavorite: List<FavoritesVideoItem>): RecyclerView.Adapter<FavoriteAdapter.MyViewHolder>() {
    class MyViewHolder(val binding: ItemFavoritesBinding): ViewHolder(binding.root) {
        fun bind(item: FavoritesVideoItem) {
            Glide.with(itemView.context)
                .load(item.thumbnailLink)
                .into(binding.shapeableImageView)

            binding.titleVideoFavorite.text = item.title

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailLearningActivity::class.java)

                val extraData = LearningModel(
                    id = item.id,
                    title = item.title,
                    description = item.description,
                    link = item.link,
                    thumbnailLink = item.thumbnailLink,
                    isFavorite = true
                )

                intent.putExtra(DetailArticleActivity.EXTRA_DATA, extraData)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val binding = ItemFavoritesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = listFavorite[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = listFavorite.size

}