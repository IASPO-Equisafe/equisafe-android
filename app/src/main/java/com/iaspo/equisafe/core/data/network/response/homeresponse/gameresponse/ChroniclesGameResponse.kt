package com.iaspo.equisafe.core.data.network.response.homeresponse.gameresponse

import com.google.gson.annotations.SerializedName

data class ChroniclesGameResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("errors")
	val errors: Boolean
)

data class QuestionsItem(

	@field:SerializedName("question_pic")
	val questionPic: Any,

	@field:SerializedName("question")
	val question: String,

	@field:SerializedName("answer")
	val answer: String
)

data class Data(

	@field:SerializedName("total_data")
	val totalData: Int,

	@field:SerializedName("questions")
	val questions: List<QuestionsItem>,

	@field:SerializedName("total_question")
	val totalQuestion: Int
)
