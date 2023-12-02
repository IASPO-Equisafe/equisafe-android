package com.iaspo.equisafe.core.data.network.response.authresponse

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @field:SerializedName("data")
	val data: Data,

    @field:SerializedName("message")
	val message: String,

    @field:SerializedName("errors")
	val errors: Boolean
)

data class Data(

	@field:SerializedName("access_token")
	val accessToken: String,

	@field:SerializedName("token_type")
	val tokenType: String
)
