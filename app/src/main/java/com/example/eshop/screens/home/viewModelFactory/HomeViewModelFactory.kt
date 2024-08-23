package com.example.eshop.screens.home.viewModelFactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.eshop.di.module.CoroutineDispatcherModule
import com.example.eshop.repository.DataRepository
import com.example.eshop.screens.home.viewModel.HomeViewModel
import javax.inject.Inject

class HomeViewModelFactory @Inject constructor(
    private val repository: DataRepository,
    private val dispatcherModule: CoroutineDispatcherModule,
    private val context: Context
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(repository, dispatcherModule, context) as T
    }


}