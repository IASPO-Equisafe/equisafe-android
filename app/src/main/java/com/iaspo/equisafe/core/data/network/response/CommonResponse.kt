package com.iaspo.equisafe.core.data.network.response

import com.google.gson.annotations.SerializedName

data class CommonResponse(

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("errors")
	val errors: Boolean
)
