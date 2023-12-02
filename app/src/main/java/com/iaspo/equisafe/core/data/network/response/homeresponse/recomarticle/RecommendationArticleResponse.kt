package com.iaspo.equisafe.core.data.network.response.homeresponse.recomarticle

import com.google.gson.annotations.SerializedName

data class RecommendationArticleResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("errors")
	val errors: Boolean? = null
)

data class Article(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("author")
	val author: String? = null,

	@field:SerializedName("_id")
	val id: String? = null,

	@field:SerializedName("source")
	val source: String? = null,

	@field:SerializedName("pic")
	val pic: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("content")
	val content: String? = null
)

data class RecomendationArticle(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("author")
	val author: String? = null,

	@field:SerializedName("_id")
	val id: String? = null,

	@field:SerializedName("source")
	val source: String? = null,

	@field:SerializedName("pic")
	val pic: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("content")
	val content: String? = null
)

data class Data(

	@field:SerializedName("article")
	val article: Article? = null,

	@field:SerializedName("recomendation_article")
	val recomendationArticle: RecomendationArticle? = null
)
