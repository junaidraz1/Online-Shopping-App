package com.example.eshop.screens.productDescription.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eshop.di.module.CoroutineDispatcherModule
import com.example.eshop.models.products.Products
import com.example.eshop.repository.DataRepository
import com.example.eshop.utils.Utility
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductDescriptionViewModel @Inject constructor(
    dataRepository: DataRepository,
    dispatcherModule: CoroutineDispatcherModule,
    context: Context,
    productId: Int
) : ViewModel() {

    val product: SharedFlow<Products> = dataRepository.productsByIdSharedFlow

    private val _isNetworkAvailable = MutableLiveData<Boolean>()
    val isNetworkAvailable: LiveData<Boolean> = _isNetworkAvailable

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    init {
        viewModelScope.launch(Utility.coroutineExceptionHandler(_errorMessage) + dispatcherModule.getIoDispatcher()) {
            val isConnected = Utility.isNetworkConnected(context)
            withContext(dispatcherModule.getMainDispatcher()) {
                _isNetworkAvailable.value = isConnected
                if (isConnected) {
                    dataRepository.getProductById(productId)
                }
            }
        }

    }
}