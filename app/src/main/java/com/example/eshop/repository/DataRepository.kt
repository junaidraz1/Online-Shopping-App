package com.example.eshop.repository

import com.example.eshop.models.cart.CartItems
import com.example.eshop.models.products.Products
import com.example.eshop.network.ApiInterface
import com.example.eshop.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DataRepository @Inject constructor(
    private val apiInterface: ApiInterface
) {

    fun getProducts(): Flow<List<Products>> = flow {
        val response = apiInterface.getRandomProducts()
        emit(response.body()!!)
    }.flowOn(Dispatchers.IO)

    fun getElectronics(): Flow<List<Products>> = flow {
        val response = apiInterface.getElectronic()
        emit(response.body()!!)
    }.flowOn(Dispatchers.IO)


    fun getJewellery(): Flow<List<Products>> = flow {
        val response = apiInterface.getJewellery()
        emit(response.body()!!)
    }.flowOn(Dispatchers.IO)

    fun getMenClothing(): Flow<List<Products>> = flow {
        val response = apiInterface.getMenClothing()
        emit(response.body()!!)
    }.flowOn(Dispatchers.IO)

    fun getWomenClothing(): Flow<List<Products>> = flow {
        val response = apiInterface.getWomenClothing()
        emit(response.body()!!)
    }.flowOn(Dispatchers.IO)

    fun getCategories(): Flow<List<String>> = flow {
        val response = apiInterface.getCategories()
        emit(response.body()!!)
    }.flowOn(Dispatchers.IO)

    private val productsByIdMutableSharedFlow = MutableSharedFlow<Products>()
    val productsByIdSharedFlow: SharedFlow<Products> = productsByIdMutableSharedFlow

    suspend fun getProductById(productId: Int) {
        val response = apiInterface.getProductById(productId)

        if (response.isSuccessful && response.body() != null) {
            productsByIdMutableSharedFlow.emit(response.body()!!)
        }
    }

    private val cartItemsMutableSharedFlow = MutableSharedFlow<List<CartItems>>()
    val cartItems: SharedFlow<List<CartItems>> = cartItemsMutableSharedFlow

    suspend fun getCartItems() {
        var itemCounter = 0
        val response = apiInterface.getCartItems()
        if (response.isSuccessful && response.body() != null) {
            for (cartItems in response.body()!!) {
                cartItemsMutableSharedFlow.emit(cartItems.products)
                getProductList(cartItems.products)
                itemCounter += cartItems.products.size
            }
            Constants.cartProductSize = itemCounter
        }
    }

    private suspend fun getProductList(productList: List<CartItems>) {
        val response = apiInterface.getCartItems()
        if (response.isSuccessful && response.body() != null) {
            for (product in productList) getProductById(product.productId)
        }
    }
}