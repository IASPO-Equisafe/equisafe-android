package com.iaspo.equisafe.core.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iaspo.equisafe.core.data.network.response.homeresponse.articleresponse.ArticlesItem
import com.iaspo.equisafe.core.utils.withNotNull
import com.iaspo.equisafe.databinding.ItemArticleBinding
import com.iaspo.equisafe.ui.home.article.DetailArticleActivity

class ArticleAdapter : PagingDataAdapter<ArticlesItem, ArticleAdapter.MyViewHolder>(DIFF_CALLBACK) {

    class MyViewHolder(private val binding: ItemArticleBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data: ArticlesItem){
            binding.titleArticle.text = data.title
            binding.descriptionArticle.text = data.content

            Glide.with(itemView.context)
                .load(data.pic)
                .into(binding.imgArticle)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailArticleActivity::class.java)
                intent.putExtra(DetailArticleActivity.EXTRA_DATA, data)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        data.withNotNull {
            holder.bind(it)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ArticlesItem>() {
            override fun areItemsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }
}