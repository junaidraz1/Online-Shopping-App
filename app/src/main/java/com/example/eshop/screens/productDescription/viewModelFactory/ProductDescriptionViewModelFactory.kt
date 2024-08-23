package com.example.eshop.screens.productDescription.viewModelFactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.eshop.di.module.CoroutineDispatcherModule
import com.example.eshop.repository.DataRepository
import com.example.eshop.screens.productDescription.viewModel.ProductDescriptionViewModel
import javax.inject.Inject

class ProductDescriptionViewModelFactory @Inject constructor(
    private val dataRepository: DataRepository,
    private val dispatcherModule: CoroutineDispatcherModule,
    private val context: Context,
    private val productId: Int
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProductDescriptionViewModel(
            dataRepository,
            dispatcherModule,
            context,
            productId
        ) as T
    }

}