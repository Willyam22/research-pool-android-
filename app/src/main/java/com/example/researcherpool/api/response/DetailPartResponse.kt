package com.example.researcherpool.api.response

import com.google.gson.annotations.SerializedName

data class DetailPartResponse(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("result")
	val result: List<DetailItem?>? = null
)

data class DetailItem(

	@field:SerializedName("id_pool")
	val idPool: Int? = null,

	@field:SerializedName("email_user")
	val emailUser: String? = null,

	@field:SerializedName("photo")
	val photo: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
