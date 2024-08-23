package com.example.eshop.screens.shoppingCart.ui

import android.annotation.SuppressLint
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
import com.example.eshop.BaseApplication
import com.example.eshop.R
import com.example.eshop.databinding.ActivityShoppingCartBinding
import com.example.eshop.di.module.CoroutineDispatcherModule
import com.example.eshop.models.products.Products
import com.example.eshop.screens.home.ui.HomeScreenActivity
import com.example.eshop.screens.shoppingCart.adapter.ShoppingCartAdapter
import com.example.eshop.screens.shoppingCart.viewModel.CartViewModel
import com.example.eshop.screens.shoppingCart.viewModelFactory.CartViewModelFactory
import com.example.eshop.utils.Constants
import com.example.eshop.utils.Utility
import com.example.eshop.utils.interfaces.BackPressListener
import com.example.eshop.utils.interfaces.ViewModelInitializer
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShoppingCartActivity : AppCompatActivity(), ViewModelInitializer, BackPressListener {

    private lateinit var binding: ActivityShoppingCartBinding
    private lateinit var cartViewModel: CartViewModel

    @Inject
    lateinit var cartViewModelFactory: CartViewModelFactory

    @Inject
    lateinit var dispatcherModule: CoroutineDispatcherModule

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShoppingCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        (application as BaseApplication).appComponent.injectCartActivity(this)
        (application as BaseApplication).appComponent.injectDispatcher()

        binding.rvCartItems.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        initializeViewModelFactory(cartViewModelFactory)

        binding.ivShoppingCartBack.setOnClickListener {
            Log.e("TAG", "onCreate: clicked")
            onCustomBackPress()
        }

        binding.layoutNoCon.ivNoConBack.setOnClickListener {
            Log.e("TAG", "onCreate: clicked")
            onCustomBackPress()
        }

    }

    override fun initializeViewModelFactory(viewModelFactory: ViewModelProvider.Factory) {
        cartViewModel =
            ViewModelProvider(this, cartViewModelFactory)[CartViewModel::class.java]

        cartViewModel.isNetworkAvailable.observe(this@ShoppingCartActivity) { isAvailable ->
            if (isAvailable) {
                observeViewModel(cartViewModel)
            } else {
                Utility.displayNoConLayout(binding.layoutNoCon, this)
            }
        }
    }

    override fun observeViewModel(viewModel: ViewModel) {
        collectData()
    }

    private fun collectData() {
        val productIdList = mutableListOf<Products>()
        val productQuantityList = mutableListOf<Int>()
        var counter = 0

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                cartViewModel.cartItem.collect { product ->
                    product.let { newProduct ->
                        for (cartProducts in newProduct) productQuantityList.add(cartProducts.quantity)
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                cartViewModel.productInCart.collect { newProduct ->

                    if ((productIdList.find { it.id == newProduct.id }) == null) {
                        productIdList.add(newProduct)
                        counter++

                    } else {
                        counter++
                    }

                    if (counter == Constants.cartProductSize) {
                        updateCartUi(productIdList.toList(), productQuantityList.toList())
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                cartViewModel.errorMessage.observe(this@ShoppingCartActivity) {
                    updateErrorUi(it)
                }
            }
        }
    }

    private fun updateErrorUi(it: String) {
        binding.layoutLoading.loadingAnim.pauseAnimation()
        binding.layoutNoCon.clNoConDisplay.visibility = View.VISIBLE
        binding.layoutLoading.clMain.visibility = View.GONE
        binding.layoutNoCon.tvNoConnection.text = getString(R.string.timeout_msg)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateCartUi(productIdList: List<Products>, productQuantityList: List<Int>) {
        binding.clMainContent.visibility = View.VISIBLE
        binding.layoutLoading.loadingAnim.visibility = View.GONE
        binding.layoutLoading.loadingAnim.pauseAnimation()

        binding.rvCartItems.adapter = ShoppingCartAdapter(
            this@ShoppingCartActivity,
            productIdList.toList(),
            productQuantityList.toList()
        )
        binding.rvCartItems.adapter?.notifyDataSetChanged()
    }

    override fun onCustomBackPress() {
        startActivity(Intent(this@ShoppingCartActivity, HomeScreenActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        })
    }
}