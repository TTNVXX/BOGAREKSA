package com.bogareksa.ui.pembeli.data.remote

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ProductResponse(

	@field:SerializedName("ProductResponse")
	val productList: List<ProductItem?>? = null
)

data class ProductItem(

	@field:SerializedName("predictionResult")
	val predictionResult: PredictionResult? = null,

	@field:SerializedName("addedDate")
	val addedDate: String? = null,

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

	@field:SerializedName("ownerId")
	val ownerId: String? = null,

	@field:SerializedName("desc")
	val desc: String? = null
): Serializable

data class PredictionResult(

	@field:SerializedName("detectedDate")
	val detectedDate: String? = null,

	@field:SerializedName("message")
	val message: String? = null
)
