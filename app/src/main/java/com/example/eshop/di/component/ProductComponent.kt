package com.example.eshop.di.component

import android.content.Context
import com.example.eshop.di.module.CoroutineDispatcherModule
import com.example.eshop.di.module.NetworkModule
import com.example.eshop.screens.productDescription.ui.ProductDescriptionActivity
import com.example.eshop.screens.shoppingCart.ui.ShoppingCartActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, CoroutineDispatcherModule::class])
interface ProductComponent {

    fun injectProductDescActivity(productDescriptionActivity: ProductDescriptionActivity)

    fun injectCartActivity(shoppingCartActivity: ShoppingCartActivity)

    fun injectDispatcher(): CoroutineDispatcherModule

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
            @BindsInstance productId: Int
        ): ProductComponent
    }
}