package com.example.eshop.screens.shoppingCart.viewModelFactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.eshop.di.module.CoroutineDispatcherModule
import com.example.eshop.repository.DataRepository
import com.example.eshop.screens.shoppingCart.viewModel.CartViewModel
import javax.inject.Inject

class CartViewModelFactory @Inject constructor(
    private val repository: DataRepository,
    private val dispatcherModule: CoroutineDispatcherModule,
    private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CartViewModel(repository, dispatcherModule, context) as T
    }
}