package com.example.eshop.network

import com.example.eshop.models.cart.Cart
import com.example.eshop.models.products.Products
import com.example.eshop.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiInterface {

    @GET(Constants.productsEndpoint)
    suspend fun getRandomProducts(): Response<List<Products>>

    @GET(Constants.productByIdEndpoint + "{productId}")
    suspend fun getProductById(@Path("productId") productId: Int): Response<Products>

    @GET(Constants.categoryEndpoint)
    suspend fun getCategories(): Response<List<String>>

    @GET(Constants.electronicsEndpoint)
    suspend fun getElectronic(): Response<List<Products>>

    @GET(Constants.jewelleryEndpoint)
    suspend fun getJewellery(): Response<List<Products>>

    @GET(Constants.menClothingEndpoint)
    suspend fun getMenClothing(): Response<List<Products>>

    @GET(Constants.womenClothingEndpoint)
    suspend fun getWomenClothing(): Response<List<Products>>

    @GET(Constants.cartItemsEndpoint)
    suspend fun getCartItems(): Response<List<Cart>>


}