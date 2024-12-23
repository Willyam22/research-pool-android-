package com.example.researcherpool.api.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("userId")
	val userId: UserId? = null
)

data class UserId(

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("Email")
	val email: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)
