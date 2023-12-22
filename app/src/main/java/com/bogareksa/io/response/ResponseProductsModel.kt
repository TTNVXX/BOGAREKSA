package com.bogareksa.io.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class MyProductsItemModel(
	val productId: String? = null,
	val imagePath: String? = null,
	val price: String? = null,
	val imageUrl: String? = null,
	val name: String? = null,
	val ownedBy: String? = null,
	val desc: String? = null
):Serializable
