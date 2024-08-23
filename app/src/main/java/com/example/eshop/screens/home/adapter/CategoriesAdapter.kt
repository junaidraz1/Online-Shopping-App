package com.example.eshop.screens.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eshop.R
import com.example.eshop.databinding.RvCategoryDesignBinding
import com.example.eshop.utils.interfaces.RecyclerClickListener

class CategoriesAdapter(
    private val mContext: Context,
    private val categoriesList: List<String>,
    private val categoriesThumbnailList: IntArray,
    private val recyclerClickListener: RecyclerClickListener
) : RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ViewHolder {
        val binding = RvCategoryDesignBinding.inflate(
            LayoutInflater.from(mContext), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(
            categoriesList[position],
            categoriesThumbnailList[position]
        )

        holder.itemView.setOnClickListener {
            recyclerClickListener.onClick("categories", holder.adapterPosition)
        }
    }

    override fun getItemCount(): Int {
        return categoriesList.size
    }

    inner class ViewHolder(private val binding: RvCategoryDesignBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(categoryName: String, thumbnail: Int) {
            when (categoryName) {
                "electronics" -> binding.tvCategoryName.text =
                    mContext.getString(R.string.electronics_lbl)

                "jewelery" -> binding.tvCategoryName.text =
                    mContext.getString(R.string.jewelery_lbl)

                "men's clothing" -> binding.tvCategoryName.text =
                    mContext.getString(R.string.men_label)

                "women's clothing" -> binding.tvCategoryName.text =
                    mContext.getString(R.string.women_lbl)
            }
            Glide.with(mContext).load(thumbnail).into(binding.ivThumbnail)
        }
    }

}