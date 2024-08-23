package com.example.eshop

import android.app.Application
import com.example.eshop.di.component.AppComponent
import com.example.eshop.di.component.DaggerAppComponent

class BaseApplication : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.factory().create(this)

    }
}