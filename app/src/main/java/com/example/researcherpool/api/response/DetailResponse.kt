package com.example.researcherpool.api.response

import com.google.gson.annotations.SerializedName

data class DetailResponse(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("result")
	val result: List<ItemDetail?>? = null
)

data class ItemDetail(

	@field:SerializedName("researcher")
	val researcher: String? = null,

	@field:SerializedName("link")
	val link: String? = null,

	@field:SerializedName("Title")
	val title: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("status")
	val status: String? = null
)
