package com.example.eshop.screens.onboard.ui

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.eshop.R
import com.example.eshop.databinding.ActivityBoardingBinding
import com.example.eshop.screens.home.ui.HomeScreenActivity
import com.example.eshop.screens.onboard.adapter.OnBoardAdapter
import com.example.eshop.utils.PrefManager

class BoardingActivity : AppCompatActivity(), OnPageChangeListener {

    private lateinit var binding: ActivityBoardingBinding
    private lateinit var boardingAdapter: OnBoardAdapter
    private var dots = arrayOfNulls<TextView>(3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        boardingAdapter = OnBoardAdapter(this)

        binding.slider.adapter = boardingAdapter
        setUpIndicator(0)
        binding.slider.addOnPageChangeListener(this)

        binding.ivNext.setOnClickListener {
            PrefManager.setOnBoardFlag(true, this)
            startActivity(Intent(this@BoardingActivity, HomeScreenActivity::class.java))
        }

    }

    private fun setUpIndicator(position: Int) {
        binding.llIndicator.removeAllViews()
        for (i in dots.indices) {
            dots[i] = TextView(this)
            dots[i]!!.text = Html.fromHtml("&#8226")
            dots[i]!!.textSize = 40f
            dots[i]!!.setTextColor(resources.getColor(R.color.onBoardColor))
            binding.llIndicator.addView(dots[i])
        }
        dots[position]?.setTextColor(resources.getColor(R.color.main_color))
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        setUpIndicator(position)

        if (position > 1) {
            binding.ivNext.visibility = View.VISIBLE

        } else {
            binding.ivNext.visibility = View.GONE
        }
    }

    override fun onPageScrollStateChanged(state: Int) {

    }
}