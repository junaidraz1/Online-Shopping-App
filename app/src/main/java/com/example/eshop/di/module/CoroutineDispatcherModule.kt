package com.example.eshop.di.module

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
class CoroutineDispatcherModule {

    @Provides
    fun provideCoroutineDispatcherModule(): CoroutineDispatcherModule {
        return CoroutineDispatcherModule()
    }

    @Singleton
    @Provides
    fun getMainDispatcher(): CoroutineDispatcher {
        return Dispatchers.Main
    }

    @Singleton
    @Provides
    fun getIoDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

}