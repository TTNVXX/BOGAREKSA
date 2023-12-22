package com.bogareksa.io.response

import com.google.gson.annotations.SerializedName

data class ResponseDeleteProduct(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("filePath")
	val filePath: String? = null
)
