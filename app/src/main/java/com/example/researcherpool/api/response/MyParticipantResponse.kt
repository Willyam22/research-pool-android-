package com.example.researcherpool.api.response

import com.google.gson.annotations.SerializedName

data class MyParticipantResponse(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("result")
	val result: List<ParticipateItem?>? = null
)

data class ParticipateItem(

	@field:SerializedName("researcher")
	val researcher: String? = null,

	@field:SerializedName("link")
	val link: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("participant_status")
	val participantStatus: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)
