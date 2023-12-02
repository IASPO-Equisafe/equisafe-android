package com.iaspo.equisafe.core.data.network.response.learningresponse

import com.google.gson.annotations.SerializedName

data class LearningResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("errors")
	val errors: Boolean? = null
)

data class Data(

	@field:SerializedName("per_page")
	val perPage: Int? = null,

	@field:SerializedName("total_data")
	val totalData: Int? = null,

	@field:SerializedName("videos")
	val videos: List<VideosItem>,

	@field:SerializedName("current_page")
	val currentPage: Int? = null
)

data class VideosItem(

	@field:SerializedName("thumbnail_link")
	val thumbnailLink: String? = null,

	@field:SerializedName("link")
	val link: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("_id")
	val id: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("is_favorite")
	val isFavorite: Boolean? = false
)
