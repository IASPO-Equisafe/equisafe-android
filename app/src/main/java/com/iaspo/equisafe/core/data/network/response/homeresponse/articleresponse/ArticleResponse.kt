package com.iaspo.equisafe.core.data.network.response.homeresponse.articleresponse

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class ArticleResponse(

    @field:SerializedName("data")
	val data: Data,

    @field:SerializedName("message")
	val message: String,

    @field:SerializedName("errors")
	val errors: Boolean
)

@Parcelize
data class ArticlesItem(

	@field:SerializedName("author")
	val author: String,

	@field:SerializedName("_id")
	val id: String,

	@field:SerializedName("source")
	val source: String,

	@field:SerializedName("pic")
	val pic: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("content")
	val content: String,

	@field:SerializedName("createdAt")
	val createdAt: String
): Parcelable

data class Data(

	@field:SerializedName("per_page")
	val perPage: Int,

	@field:SerializedName("total_data")
	val totalData: Int,

	@field:SerializedName("articles")
	val articles: List<ArticlesItem>,

	@field:SerializedName("current_page")
	val currentPage: Int
)
