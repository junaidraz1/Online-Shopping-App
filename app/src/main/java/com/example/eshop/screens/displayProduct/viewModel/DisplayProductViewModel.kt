package com.example.eshop.screens.displayProduct.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eshop.di.module.CoroutineDispatcherModule
import com.example.eshop.models.products.Products
import com.example.eshop.repository.DataRepository
import com.example.eshop.utils.Constants
import com.example.eshop.utils.Utility
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DisplayProductViewModel(
    dataRepository: DataRepository, dispatcherModule: CoroutineDispatcherModule, context: Context
) : ViewModel() {

    private val _isNetworkAvailable = MutableLiveData<Boolean>()
    val isNetworkAvailable: LiveData<Boolean>
        get() = _isNetworkAvailable

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val products: MutableLiveData<List<Products>> = MutableLiveData()
    var productsLiveData: LiveData<List<Products>> = products

    init {
        when (Constants.selectedCategory) {
            0 -> {
                getElectronics(dataRepository, dispatcherModule, context)
            }

            1 -> {
                getJewellery(dataRepository, dispatcherModule, context)
            }

            2 -> {
                getMenClothing(dataRepository, dispatcherModule, context)
            }

            3 -> {
                getWomenClothing(dataRepository, dispatcherModule, context)
            }
        }
    }

    private fun getElectronics(
        dataRepository: DataRepository,
        dispatcherModule: CoroutineDispatcherModule,
        context: Context
    ) {
        viewModelScope.launch(Utility.coroutineExceptionHandler(_errorMessage) + dispatcherModule.getIoDispatcher()) {
            val isConnected = Utility.isNetworkConnected(context)
            withContext(dispatcherModule.getMainDispatcher()) {
                _isNetworkAvailable.postValue(isConnected)
                if (isConnected) {
                    dataRepository.getElectronics().collect {
                        products.postValue(it)
                    }
                }
            }
        }
    }

    private fun getJewellery(
        dataRepository: DataRepository,
        dispatcherModule: CoroutineDispatcherModule,
        context: Context
    ) {
        viewModelScope.launch(Utility.coroutineExceptionHandler(_errorMessage) + dispatcherModule.getIoDispatcher()) {
            val isConnected = Utility.isNetworkConnected(context)
            withContext(dispatcherModule.getMainDispatcher()) {
                _isNetworkAvailable.postValue(isConnected)
                if (isConnected) {
                    dataRepository.getJewellery().collect {
                        products.postValue(it)
                    }
                }
            }
        }

    }

    private fun getMenClothing(
        dataRepository: DataRepository,
        dispatcherModule: CoroutineDispatcherModule,
        context: Context
    ) {
        viewModelScope.launch(Utility.coroutineExceptionHandler(_errorMessage) + dispatcherModule.getIoDispatcher()) {
            val isConnected = Utility.isNetworkConnected(context)
            withContext(dispatcherModule.getMainDispatcher()) {
                _isNetworkAvailable.postValue(isConnected)
                if (isConnected) {
                    dataRepository.getMenClothing().collect {
                        products.postValue(it)
                    }
                }
            }
        }

    }

    private fun getWomenClothing(
        dataRepository: DataRepository,
        dispatcherModule: CoroutineDispatcherModule,
        context: Context
    ) {
        viewModelScope.launch(Utility.coroutineExceptionHandler(_errorMessage) + dispatcherModule.getIoDispatcher()) {
            val isConnected = Utility.isNetworkConnected(context)
            withContext(dispatcherModule.getMainDispatcher()) {
                _isNetworkAvailable.postValue(isConnected)
                if (isConnected) {
                    dataRepository.getWomenClothing().collect {
                        products.postValue(it)
                    }
                }
            }
        }
    }
}