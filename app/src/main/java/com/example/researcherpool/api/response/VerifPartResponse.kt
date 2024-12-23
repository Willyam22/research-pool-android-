package com.example.researcherpool.api.response

import com.google.gson.annotations.SerializedName

data class VerifPartResponse(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("photo")
	val photo: String? = null,

	@field:SerializedName("status")
	val status:String? = null
)
