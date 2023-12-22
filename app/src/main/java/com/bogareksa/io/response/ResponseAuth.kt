package com.bogareksa.io.response

import com.google.gson.annotations.SerializedName

data class ResponseAuth(

	@field:SerializedName("apiToken")
	val apiToken: String? = null,


	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("loginDetail")
	val loginDetail: LoginDetail? = null,

	@field:SerializedName("desc")
	val desc: String? = null
)

data class ProviderUserInfoItem(

	@field:SerializedName("federatedId")
	val federatedId: String? = null,

	@field:SerializedName("providerId")
	val providerId: String? = null,

	@field:SerializedName("rawId")
	val rawId: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)

data class LoginDetail(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("emailVerified")
	val emailVerified: Boolean? = null,

	@field:SerializedName("passwordUpdatedAt")
	val passwordUpdatedAt: Long? = null,

	@field:SerializedName("lastLoginAt")
	val lastLoginAt: String? = null,

	@field:SerializedName("validSince")
	val validSince: String? = null,

	@field:SerializedName("providerUserInfo")
	val providerUserInfo: List<ProviderUserInfoItem?>? = null,

	@field:SerializedName("lastRefreshAt")
	val lastRefreshAt: String? = null,

	@field:SerializedName("localId")
	val localId: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("passwordHash")
	val passwordHash: String? = null,

	@field:SerializedName("role")
	val role: Int? = null
)
