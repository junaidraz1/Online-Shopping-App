package com.example.eshop.screens.displayProduct.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.eshop.BaseApplication
import com.example.eshop.R
import com.example.eshop.databinding.ActivityDisplayProductBinding
import com.example.eshop.models.products.Products
import com.example.eshop.screens.displayProduct.viewModel.DisplayProductViewModel
import com.example.eshop.screens.displayProduct.viewModelFactory.DisplayProductViewModelFactory
import com.example.eshop.screens.home.adapter.ProductsAdapter
import com.example.eshop.screens.home.ui.HomeScreenActivity
import com.example.eshop.screens.productDescription.ui.ProductDescriptionActivity
import com.example.eshop.utils.Constants
import com.example.eshop.utils.Utility
import com.example.eshop.utils.interfaces.BackPressListener
import com.example.eshop.utils.interfaces.RecyclerClickListener
import com.example.eshop.utils.interfaces.ViewModelInitializer
import kotlinx.coroutines.launch
import javax.inject.Inject

class DisplayProductActivity : AppCompatActivity(), RecyclerClickListener, ViewModelInitializer,
    BackPressListener {

    private lateinit var binding: ActivityDisplayProductBinding
    private lateinit var displayProductViewModel: DisplayProductViewModel

    private lateinit var activityIntent: Intent

    @Inject
    lateinit var displayProductViewModelFactory: DisplayProductViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDisplayProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        (application as BaseApplication).appComponent.injectDisplayProductActivity(this)
        (application as BaseApplication).appComponent.injectDispatcher()

        setViews(Constants.selectedCategory)

        setClickListeners()

    }

    private fun setClickListeners() {
        Log.e("TAG", "setClickListeners: inside")
        binding.ivActivityBack.setOnClickListener {
            Log.e("TAG", "setClickListeners: working")
            startActivity(Intent(
                this@DisplayProductActivity, HomeScreenActivity::class.java
            ).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            })
        }

        binding.layoutNoCon.ivNoConBack.setOnClickListener {
            onCustomBackPress()
        }
    }

    private fun setViews(value: Int) {
        when (value) {
            0 -> { //Electronics
                binding.ivHeaderImage.setImageResource(R.drawable.ic_electronics_section_bg)
                binding.tvHeaderName.text = getString(R.string.electronics_lbl)
            }

            1 -> {//Jewellery
                binding.ivHeaderImage.setImageResource(R.drawable.ic_jewelery_section_bg)
                binding.tvHeaderName.text = getString(R.string.jewelery_lbl)
            }

            2 -> {//Men's Clothing
                binding.ivHeaderImage.setImageResource(R.drawable.ic_men_section_bg)
                binding.tvHeaderName.text = getString(R.string.men_label)
            }

            3 -> {//Women's Clothing
                binding.ivHeaderImage.setImageResource(R.drawable.ic_women_section_bg)
                binding.tvHeaderName.text = getString(R.string.women_lbl)
            }
        }
        binding.rvCategoryProduct.layoutManager =
            StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        initializeViewModelFactory(displayProductViewModelFactory)
    }

    override fun initializeViewModelFactory(viewModelFactory: ViewModelProvider.Factory) {
        displayProductViewModel = ViewModelProvider(
            this, displayProductViewModelFactory
        )[DisplayProductViewModel::class.java]

        displayProductViewModel.isNetworkAvailable.observe(this@DisplayProductActivity) { isAvailable ->
            if (isAvailable) {
                observeViewModel(displayProductViewModel)

            } else {
                Utility.displayNoConLayout(binding.layoutNoCon, this)
            }
        }
    }

    override fun observeViewModel(viewModel: ViewModel) {
        collectData()
    }

    private fun collectData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                displayProductViewModel.productsLiveData.observe(this@DisplayProductActivity) {
                    updateProductsUi(it)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                displayProductViewModel.errorMessage.observe(this@DisplayProductActivity) {
                    updateErrorUi(it)
                }
            }
        }
    }

    private fun updateProductsUi(it: List<Products>) {
        it.let {
            binding.clMainContent.visibility = View.VISIBLE
            binding.layoutLoading.loadingAnim.visibility = View.GONE
            binding.layoutLoading.loadingAnim.pauseAnimation()

            binding.rvCategoryProduct.adapter =
                ProductsAdapter(this@DisplayProductActivity, it, this)
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

    override fun onClick(from: String, position: Int) {
        activityIntent = Intent(this@DisplayProductActivity, ProductDescriptionActivity::class.java)
        activityIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        Constants.isFrom = "categories"
        Constants.selectedProductId = position
        startActivity(activityIntent)
    }

    override fun onCustomBackPress() {
        startActivity(Intent(this@DisplayProductActivity, HomeScreenActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        })
    }
}