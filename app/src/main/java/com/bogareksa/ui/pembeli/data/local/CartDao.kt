package com.bogareksa.ui.pembeli.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addToCart(cart: CartEntity)

    @Query("SELECT * FROM cart_table")
    fun getAllCartItems(): Flow<List<CartEntity>>

    @Query("SELECT * FROM cart_table WHERE cartId = :productId")
    suspend fun getOrderProductById(productId: String): CartEntity?

    @Delete
    fun delete(cart: CartEntity)
}