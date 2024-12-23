package com.example.researcherpool.api.response

import com.google.gson.annotations.SerializedName

data class CountResponse(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("result")
	val result: List<CountItem?>? = null
)

data class CountItem(

	@field:SerializedName("Decline")
	val decline: String? = null,

	@field:SerializedName("Approved")
	val approved: String? = null,

	@field:SerializedName("Pending")
	val pending: String? = null
)
