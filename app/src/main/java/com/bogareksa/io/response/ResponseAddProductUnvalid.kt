package com.bogareksa.io.response

import com.google.gson.annotations.SerializedName

data class ResponseAddProductUnvalid(

	@field:SerializedName("data")
	val data: DataUnvalid? = null,

	@field:SerializedName("status")
	val status: StatusUnvalid? = null
)

data class DataUnvalid(

	@field:SerializedName("detectedDate")
	val detectedDate: Any? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class StatusUnvalid(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("code")
	val code: Int? = null
)
