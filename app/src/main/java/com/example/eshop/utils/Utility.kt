package com.example.eshop.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.example.eshop.R
import com.example.eshop.databinding.LayoutNoConnectionMessageBinding
import com.example.eshop.utils.interfaces.BackPressListener
import kotlinx.coroutines.CoroutineExceptionHandler
import java.util.concurrent.TimeoutException


object Utility {

    fun isNetworkConnected(context: Context): Boolean {

        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        val allNetworks = manager?.allNetworks ?: return false
        allNetworks.forEach { network ->
            val info = manager.getNetworkInfo(network)
            if (info!!.state == NetworkInfo.State.CONNECTED) return true
        }
        return false
    }

    fun getThumbnails(): IntArray {
        return intArrayOf(
            R.drawable.ic_electronics,
            R.drawable.ic_jewellery,
            R.drawable.ic_men,
            R.drawable.ic_women
        )
    }

    fun getOnBoardImages(): IntArray {
        return intArrayOf(
            R.drawable.ic_onboard_1,
            R.drawable.ic_onboard_2,
            R.drawable.ic_onboard_3
        )
    }

    fun getOnBoardMessage(): IntArray {
        return intArrayOf(
            R.string.on_board_msg_1,
            R.string.on_board_msg_2,
            R.string.on_board_msg_3
        )
    }

    fun displayNoConLayout(
        binding: LayoutNoConnectionMessageBinding,
        backPressListener: BackPressListener
    ) {
        if (!Constants.isErrorScreenDisplayed) {
            binding.clNoConDisplay.visibility = View.VISIBLE
            binding.ivNoConBack.setOnClickListener { backPressListener.onCustomBackPress() }
            Constants.isErrorScreenDisplayed = true
        }
    }

    fun coroutineExceptionHandler(errorMessage: MutableLiveData<String>): CoroutineExceptionHandler {
        val handler = CoroutineExceptionHandler { _, throwable ->
            when (throwable) {
                is TimeoutException -> {
                    errorMessage.postValue("Request timed out. Please try again later.")
                }

                else -> {
                    errorMessage.postValue("An error occurred. Please try again later.")
                }
            }
        }
        return handler
    }
}