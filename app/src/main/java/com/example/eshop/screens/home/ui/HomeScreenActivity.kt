package com.example.eshop.screens.home.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.eshop.BaseApplication
import com.example.eshop.R
import com.example.eshop.databinding.ActivityHomeScreenBinding
import com.example.eshop.di.module.CoroutineDispatcherModule
import com.example.eshop.models.products.Products
import com.example.eshop.screens.displayProduct.ui.DisplayProductActivity
import com.example.eshop.screens.home.adapter.CategoriesAdapter
import com.example.eshop.screens.home.adapter.ProductsAdapter
import com.example.eshop.screens.home.viewModel.HomeViewModel
import com.example.eshop.screens.home.viewModelFactory.HomeViewModelFactory
import com.example.eshop.screens.productDescription.ui.ProductDescriptionActivity
import com.example.eshop.screens.shoppingCart.ui.ShoppingCartActivity
import com.example.eshop.utils.Constants
import com.example.eshop.utils.Utility
import com.example.eshop.utils.interfaces.BackPressListener
import com.example.eshop.utils.interfaces.RecyclerClickListener
import com.example.eshop.utils.interfaces.ViewModelInitializer
import javax.inject.Inject

class HomeScreenActivity : AppCompatActivity(), ViewModelInitializer, BackPressListener,
    RecyclerClickListener {

    private lateinit var binding: ActivityHomeScreenBinding
    private lateinit var homeViewModel: HomeViewModel

    private lateinit var activityIntent: Intent

    @Inject
    lateinit var homeViewModelFactory: HomeViewModelFactory

    @Inject
    lateinit var dispatcherModule: CoroutineDispatcherModule

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        (application as BaseApplication).appComponent.injectHomeActivity(this)
        (application as BaseApplication).appComponent.injectDispatcher()

        binding.rvCategories.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        binding.rvProductRecyclerView.layoutManager =
            StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)

        initializeViewModelFactory(homeViewModelFactory)

        binding.ivShoppingCart.setOnClickListener {
            startActivity(Intent(this@HomeScreenActivity, ShoppingCartActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            })
        }

        binding.layoutNoCon.ivNoConBack.setOnClickListener {
            onCustomBackPress()
        }
    }

    override fun initializeViewModelFactory(viewModelFactory: ViewModelProvider.Factory) {
        homeViewModel = ViewModelProvider(
            this, homeViewModelFactory
        )[HomeViewModel::class.java]

        homeViewModel.isNetworkAvailable.observe(this@HomeScreenActivity) { isAvailable ->
            if (isAvailable) {
                observeViewModel(homeViewModel)

            } else {
                Utility.displayNoConLayout(binding.layoutNoCon, this)
            }
        }
    }

    override fun observeViewModel(viewModel: ViewModel) {
        collectData()
    }

    private fun collectData() {
        homeViewModel.categoriesLiveData.observe(this@HomeScreenActivity) {
            updateCategoriesUi(it)
        }

        homeViewModel.productsLiveData.observe(this@HomeScreenActivity) {
            updateProductUi(it)
        }

        homeViewModel.errorMessage.observe(this@HomeScreenActivity) {
            updateErrorMessageUi(it)
        }
    }

    private fun updateProductUi(it: List<Products>) {
        it.let {
            binding.rvProductRecyclerView.visibility = View.VISIBLE
            binding.dealsAnim.visibility = View.GONE
            binding.dealsAnim.pauseAnimation()

            binding.rvProductRecyclerView.adapter = ProductsAdapter(
                this@HomeScreenActivity,
                it, this
            )
        }
    }

    private fun updateCategoriesUi(it: List<String>) {
        Log.e("TAG", "updateCategoriesUi: ")
        it.let {
            binding.rvCategories.visibility = View.VISIBLE
            binding.categoriesAnim.visibility = View.GONE
            binding.categoriesAnim.pauseAnimation()

            binding.rvCategories.adapter = CategoriesAdapter(
                this@HomeScreenActivity,
                it,
                Utility.getThumbnails(), this
            )
        }
    }

    private fun updateErrorMessageUi(it: String) {
        it.let {
            binding.categoriesAnim.pauseAnimation()
            binding.layoutNoCon.clNoConDisplay.visibility = View.VISIBLE
            binding.svScroll.visibility = View.GONE
            binding.layoutNoCon.tvNoConnection.text = getString(R.string.timeout_msg)
        }
    }

    override fun onClick(from: String, position: Int) {
        when (from) {
            "categories" -> {
                activityIntent = Intent(
                    this@HomeScreenActivity, DisplayProductActivity::class.java
                )
                Constants.isFrom = "categories"
                Constants.selectedCategory = position
                startActivity(activityIntent)
            }

            "products" -> {
                activityIntent = Intent(
                    this@HomeScreenActivity, ProductDescriptionActivity::class.java
                )
                Constants.isFrom = "products"
                Constants.selectedProductId = position
                startActivity(activityIntent)
            }
        }
    }

    override fun onCustomBackPress() {
        finishAffinity()
    }

}