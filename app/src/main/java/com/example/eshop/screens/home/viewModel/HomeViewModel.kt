package com.example.eshop.screens.home.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eshop.di.module.CoroutineDispatcherModule
import com.example.eshop.models.products.Products
import com.example.eshop.repository.DataRepository
import com.example.eshop.utils.Utility
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(
    dataRepository: DataRepository,
    dispatcherModule: CoroutineDispatcherModule,
    context: Context
) : ViewModel() {

    private val _isNetworkAvailable = MutableLiveData<Boolean>()
    val isNetworkAvailable: LiveData<Boolean>
        get() = _isNetworkAvailable

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    init {
        getCategories(dataRepository, dispatcherModule, context)
        getProducts(dataRepository, dispatcherModule, context)
    }

    private val products: MutableLiveData<List<Products>> = MutableLiveData()
    val productsLiveData: LiveData<List<Products>> = products

    private fun getProducts(
        dataRepository: DataRepository,
        dispatcherModule: CoroutineDispatcherModule,
        context: Context
    ) {
        viewModelScope.launch(Utility.coroutineExceptionHandler(_errorMessage) + dispatcherModule.getIoDispatcher()) {
            val isConnected = Utility.isNetworkConnected(context)
            withContext(dispatcherModule.getMainDispatcher()) {
                _isNetworkAvailable.postValue(isConnected)
                if (isConnected) {
                    dataRepository.getProducts().collect {
                        products.postValue(it)
                    }
                }
            }
        }
    }

    private val categories: MutableLiveData<List<String>> = MutableLiveData()
    val categoriesLiveData: LiveData<List<String>> = categories

    private fun getCategories(
        dataRepository: DataRepository,
        dispatcherModule: CoroutineDispatcherModule,
        context: Context
    ) {
        viewModelScope.launch(Utility.coroutineExceptionHandler(_errorMessage) + dispatcherModule.getIoDispatcher()) {
            val isConnected = Utility.isNetworkConnected(context)
            withContext(dispatcherModule.getMainDispatcher()) {
                _isNetworkAvailable.postValue(isConnected)
                if (isConnected) {
                    dataRepository.getCategories()
                        .collect { categoriesResponse ->
                            categories.postValue(categoriesResponse)
                        }
                }
            }
        }
    }
}