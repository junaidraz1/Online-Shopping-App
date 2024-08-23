package com.example.eshop.screens.shoppingCart.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eshop.databinding.RvCartItemDesignBinding
import com.example.eshop.models.products.Products

class ShoppingCartAdapter(
    private val mContext: Context, private val cartItemList: List<Products>,
    private val cartItemQuantityList: List<Int>
) : RecyclerView.Adapter<ShoppingCartAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RvCartItemDesignBinding.inflate(
            LayoutInflater.from(mContext), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return cartItemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cartItemList[position], cartItemQuantityList[position])
    }

    inner class ViewHolder(private val binding: RvCartItemDesignBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(productDetails: Products, productQuantity: Int) {
            binding.tvProductName.text = productDetails.title
            binding.tvProductPrice.text = "$ ${productDetails.price.toString()}"
            binding.tvProductQuantity.text = productQuantity.toString()

            Glide.with(mContext).load(productDetails.image).into(binding.ivProductImage)
        }
    }

}