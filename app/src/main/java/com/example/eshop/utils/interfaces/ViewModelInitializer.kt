package com.example.eshop.utils.interfaces

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

interface ViewModelInitializer {

    fun initializeViewModelFactory(viewModelFactory: ViewModelProvider.Factory)

    fun observeViewModel(viewModel: ViewModel)
}