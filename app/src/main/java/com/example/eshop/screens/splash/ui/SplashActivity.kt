package com.example.eshop.screens.splash.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.eshop.databinding.ActivitySplashBinding
import com.example.eshop.screens.home.ui.HomeScreenActivity
import com.example.eshop.screens.onboard.ui.BoardingActivity
import com.example.eshop.utils.PrefManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        CoroutineScope(Dispatchers.Main).launch {
            hideProgressBar()
        }

        binding.btnContinue.setOnClickListener {
            when (PrefManager.getOnBoardFlag(this)) {
                true -> startActivity(Intent(this, HomeScreenActivity::class.java))
                false -> startActivity(Intent(this, BoardingActivity::class.java))
            }
        }
    }

    private suspend fun hideProgressBar() {
        delay(1000)
        binding.pbProgressBar.visibility = View.GONE
        binding.btnContinue.visibility = View.VISIBLE
    }
}