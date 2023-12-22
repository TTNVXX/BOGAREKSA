package com.bogareksa.ui.pembeli.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_table")
data class CartEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "cartId")
    val id: Int = 0,

    @field:ColumnInfo(name = "imageUrl")
    var imageUrl: String = "",

    @field:ColumnInfo(name = "name")
    var name: String = "",

    @field:ColumnInfo(name = "price")
    var price: Int = 0,

    @field:ColumnInfo(name = "amount")
    val amount: Int
)

