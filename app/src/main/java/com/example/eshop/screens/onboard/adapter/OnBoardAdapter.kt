package com.example.eshop.screens.onboard.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.example.eshop.databinding.LayoutOnBoardBinding
import com.example.eshop.utils.Utility

class OnBoardAdapter(private val context: Context) : PagerAdapter() {
    
    override fun getCount(): Int {
        return Utility.getOnBoardImages().size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val binding = LayoutOnBoardBinding.inflate(LayoutInflater.from(context), container, false)

        binding.tvMessage.setText(Utility.getOnBoardMessage()[position])

        Glide.with(context).load(Utility.getOnBoardImages()[position]).into(binding.ivImage)

        container.addView(binding.root)
        return binding.root
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

}