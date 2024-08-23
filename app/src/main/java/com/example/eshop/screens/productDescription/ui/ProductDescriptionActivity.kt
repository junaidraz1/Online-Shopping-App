package com.example.eshop.screens.productDescription.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.example.eshop.R
import com.example.eshop.databinding.ActivityProductDescriptionBinding
import com.example.eshop.di.component.DaggerProductComponent
import com.example.eshop.di.component.ProductComponent
import com.example.eshop.models.products.Products
import com.example.eshop.screens.displayProduct.ui.DisplayProductActivity
import com.example.eshop.screens.home.ui.HomeScreenActivity
import com.example.eshop.screens.productDescription.viewModel.ProductDescriptionViewModel
import com.example.eshop.screens.productDescription.viewModelFactory.ProductDescriptionViewModelFactory
import com.example.eshop.utils.Constants
import com.example.eshop.utils.Utility
import com.example.eshop.utils.interfaces.BackPressListener
import com.example.eshop.utils.interfaces.ViewModelInitializer
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProductDescriptionActivity : AppCompatActivity(), ViewModelInitializer, BackPressListener {

    private lateinit var binding: ActivityProductDescriptionBinding
    private lateinit var activityComponent: ProductComponent
    private lateinit var productDescriptionViewModel: ProductDescriptionViewModel
    private lateinit var activityIntent: Intent

    @Inject
    lateinit var productDescriptionViewModelFactory: ProductDescriptionViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activityComponent =
            DaggerProductComponent.factory().create(this, Constants.selectedProductId)

        activityComponent.injectProductDescActivity(this)
        activityComponent.injectDispatcher()

        initializeViewModelFactory(productDescriptionViewModelFactory)

        binding.btnContinueShopping.setOnClickListener {
            when (Constants.isFrom) {
                "products" -> {
                    activityIntent = Intent(
                        this@ProductDescriptionActivity, HomeScreenActivity::class.java
                    ).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    }
                    startActivity(activityIntent)
                }

                "categories" -> {
                    activityIntent = Intent(
                        this@ProductDescriptionActivity, DisplayProductActivity::class.java
                    ).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    }
                    startActivity(activityIntent)
                }
            }
        }

        binding.layoutNoCon.ivNoConBack.setOnClickListener {
            onCustomBackPress()
        }
    }

    override fun initializeViewModelFactory(viewModelFactory: ViewModelProvider.Factory) {
        when (viewModelFactory) {
            is ProductDescriptionViewModelFactory -> {

                productDescriptionViewModel = ViewModelProvider(
                    this, productDescriptionViewModelFactory
                )[ProductDescriptionViewModel::class.java]

                productDescriptionViewModel.isNetworkAvailable.observe(this@ProductDescriptionActivity) { isAvailable ->
                    if (isAvailable) {
                        observeViewModel(productDescriptionViewModel)
                    } else {
                        Utility.displayNoConLayout(
                            binding.layoutNoCon,
                            this
                        )
                    }
                }
            }
        }
    }

    override fun observeViewModel(viewModel: ViewModel) {
        collectData()
    }

    private fun collectData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                productDescriptionViewModel.product.collect {
                    updateProductUi(it)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                productDescriptionViewModel.errorMessage.observe(this@ProductDescriptionActivity) {
                    updateErrorUi(it)
                }
            }
        }
    }

    private fun updateProductUi(products: Products) {
        products.let {
            binding.clMainContent.visibility = View.VISIBLE
            binding.layoutLoading.loadingAnim.visibility = View.GONE
            binding.layoutLoading.loadingAnim.pauseAnimation()

            Glide.with(this@ProductDescriptionActivity).load(it.image)
                .into(binding.ivProductImage)
            binding.tvProductTitle.text = it.title
            binding.tvProductDescription.text = it.description
            binding.tvRating.text = it.rating.rate.toString()
            binding.tvStockCount.text = it.rating.count.toString()
            binding.tvProductPrice.text =
                getString(R.string.dollar_sign_lbl, it.price.toString())
        }
    }

    private fun updateErrorUi(it: String) {
        it.let {
            binding.layoutLoading.loadingAnim.pauseAnimation()
            binding.layoutNoCon.clNoConDisplay.visibility = View.VISIBLE
            binding.layoutLoading.clMain.visibility = View.GONE
            binding.layoutNoCon.tvNoConnection.text = getString(R.string.timeout_msg)
        }
    }

    override fun onCustomBackPress() {
        startActivity(Intent(
            this@ProductDescriptionActivity, HomeScreenActivity::class.java
        ).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        })
    }

}