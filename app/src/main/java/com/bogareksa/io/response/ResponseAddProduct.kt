package com.bogareksa.io.response

import com.google.gson.annotations.SerializedName

data class ResponseAddProduct(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("status")
	val status: Status? = null
)

data class Data(


	@field:SerializedName("productId")
	val productId: String? = null,

	@field:SerializedName("imagePath")
	val imagePath: String? = null,

	@field:SerializedName("price")
	val price: Int? = null,

	@field:SerializedName("imageUrl")
	val imageUrl: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("ownedBy")
	val ownedBy: String? = null,

	@field:SerializedName("predictedData")
	val predictedData: PredictedData? = null,

	@field:SerializedName("desc")
	val desc: String? = null,

	@field:SerializedName("detectedDate")
	val detectedDate: String? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class Status(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("code")
	val code: Int? = null
)

data class PredictedData(

	@field:SerializedName("data")
	val data: Any? = null,

	@field:SerializedName("detectedDate")
	val detectedDate: String? = null,

	@field:SerializedName("message")
	val message: String? = null
)