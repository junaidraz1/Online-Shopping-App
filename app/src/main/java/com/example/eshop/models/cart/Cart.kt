package com.example.eshop.models.cart

data class Cart(
    val __v: Int,
    val date: String,
    val id: Int,
    val products: List<CartItems>,
    val userId: Int
)