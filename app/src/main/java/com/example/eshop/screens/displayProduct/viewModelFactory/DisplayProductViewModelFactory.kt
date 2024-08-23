package com.example.eshop.screens.displayProduct.viewModelFactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.eshop.di.module.CoroutineDispatcherModule
import com.example.eshop.repository.DataRepository
import com.example.eshop.screens.displayProduct.viewModel.DisplayProductViewModel
import javax.inject.Inject

class DisplayProductViewModelFactory @Inject constructor(
    private val dataRepository: DataRepository,
    private val dispatcherModule: CoroutineDispatcherModule,
    private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DisplayProductViewModel(dataRepository, dispatcherModule, context) as T
    }

}