package com.example.eshop.screens.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eshop.databinding.RvDealsProductDesignBinding
import com.example.eshop.models.products.Products
import com.example.eshop.utils.interfaces.RecyclerClickListener

class ProductsAdapter(
    private val mContext: Context,
    private val mList: List<Products>,
    private val recyclerClickListener: RecyclerClickListener
) : RecyclerView.Adapter<ProductsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RvDealsProductDesignBinding.inflate(
            LayoutInflater.from(mContext), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mList[position])

        holder.itemView.setOnClickListener {
            recyclerClickListener.onClick("products", mList[position].id)
        }

    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class ViewHolder(private val binding: RvDealsProductDesignBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Products) {
            binding.tvProductName.text = item.title
            binding.tvProductPrice.text = "$ ${item.price.toString()}"

            Glide.with(mContext).load(item.image).into(binding.ivProductImage)
        }
    }
}
