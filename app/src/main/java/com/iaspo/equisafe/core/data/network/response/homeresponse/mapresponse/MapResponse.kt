package com.iaspo.equisafe.core.data.network.response.homeresponse.mapresponse

import com.google.gson.annotations.SerializedName

data class MapResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("errors")
	val errors: Boolean
)

data class Data(

	@field:SerializedName("per_page")
	val perPage: Int,

	@field:SerializedName("maps")
	val maps: List<MapsItem>,

	@field:SerializedName("total_data")
	val totalData: Int,

	@field:SerializedName("current_page")
	val currentPage: Int
)

data class MapsItem(

	@field:SerializedName("nama")
	val nama: String,

	@field:SerializedName("data")
	val data: List<DataItem>? = null,

	@field:SerializedName("_id")
	val id: String,

	@field:SerializedName("long")
	val long: Double,

	@field:SerializedName("lat")
	val lat: Double
)

data class DataItem(

	@field:SerializedName("total")
	val total: Int,

	@field:SerializedName("nama")
	val nama: String,

	@field:SerializedName("_id")
	val id: String
)
