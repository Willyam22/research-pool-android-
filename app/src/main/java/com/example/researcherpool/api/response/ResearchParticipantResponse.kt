package com.example.researcherpool.api.response

import com.google.gson.annotations.SerializedName

data class ResearchParticipantResponse(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("result")
	val result: List<PartR?>? = null
)

data class PartR(

	@field:SerializedName("id_pool")
	val idPool: Int? = null,

	@field:SerializedName("email_user")
	val emailUser: String? = null,

	@field:SerializedName("user_username")
	val userUsername: String? = null,

	@field:SerializedName("photo")
	val photo: String? = null,

	@field:SerializedName("researcher_username")
	val researcherUsername: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("title")
	val title: String? = null
)
