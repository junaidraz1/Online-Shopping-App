package com.example.eshop.utils

object Constants {
    const val baseURL = "https://fakestoreapi.com"
    const val productsEndpoint = "/products"
    const val productByIdEndpoint = "/products/"
    const val categoryEndpoint = "/products/categories"
    const val menClothingEndpoint = "/products/category/men's%20clothing"
    const val womenClothingEndpoint = "/products/category/women's%20clothing"
    const val jewelleryEndpoint = "/products/category/jewelery"
    const val electronicsEndpoint = "/products/category/electronics"
    const val cartItemsEndpoint = "/carts"

    var isErrorScreenDisplayed = false

    var isFrom = ""
    var selectedCategory = 0
    var selectedProductId = 0
    var cartProductSize = 0

}