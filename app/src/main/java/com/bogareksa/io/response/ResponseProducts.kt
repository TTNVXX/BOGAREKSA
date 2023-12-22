package com.bogareksa.io.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ResponseProducts(

	@field:SerializedName("myProducts")
	val myProducts: List<MyProductsItem>
)

data class MyProductsItem(

	@field:SerializedName("productId")
	val productId: String? = null,

	@field:SerializedName("imagePath")
	val imagePath: String? = null,

	@field:SerializedName("price")
	val price: String? = null,

	@field:SerializedName("imageUrl")
	val imageUrl: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("ownedBy")
	val ownedBy: String? = null,

	@field:SerializedName("desc")
	val desc: String? = null
):Serializable
