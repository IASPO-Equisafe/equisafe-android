package com.iaspo.equisafe.core.data.network.response.accountresponse

import com.google.gson.annotations.SerializedName

data class AccountResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("errors")
	val errors: Boolean? = null
)

data class LastVideo(

	@field:SerializedName("link")
	val link: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("_id")
	val id: String? = null,

	@field:SerializedName("title")
	val title: String? = null
)

data class Data(

	@field:SerializedName("last_video")
	val lastVideo: LastVideo? = null,

	@field:SerializedName("role")
	val role: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("_id")
	val id: String? = null,

	@field:SerializedName("username")
	val username: String? = null,

	@field:SerializedName("emergency_contact")
	val emergencyContact: EmergencyContact? = null
)

data class EmergencyContact(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("no_telp")
	val noTelp: String? = null
)
