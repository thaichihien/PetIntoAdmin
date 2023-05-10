package com.mobye.petintoadmin.views.fragments

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.mobye.petintoadmin.R
import com.mobye.petintoadmin.adapters.ProductPagingAdapter
import com.mobye.petintoadmin.databinding.FragmentAddProductOrderBinding
import com.mobye.petintoadmin.repositories.OrderRepository
import com.mobye.petintoadmin.repositories.ProductRepository
import com.mobye.petintoadmin.utils.Utils
import com.mobye.petintoadmin.viewmodels.AdminViewModelFactory
import com.mobye.petintoadmin.viewmodels.OrderViewModel
import com.mobye.petintoadmin.viewmodels.ProductViewModel
import com.mobye.petintoadmin.views.changeToFail
import com.mobye.petintoadmin.views.changeToSuccess
import kotlinx.coroutines.flow.collectLatest


class AddProductOrderFragment : BaseFragment<FragmentAddProductOrderBinding>() {

    private val productViewModel : ProductViewModel by activityViewModels {
        AdminViewModelFactory(ProductRepository())
    }
    private val orderViewModel : OrderViewModel by activityViewModels {
        AdminViewModelFactory(OrderRepository())
    }

    private lateinit var productAdapter : ProductPagingAdapter
    private var editMode = false

    private val notiDialog : Dialog by lazy {
        Dialog(requireContext()).apply {
            setCancelable(true)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.notification_dialog)
            findViewById<Button>(R.id.btnClose).setOnClickListener{
                this.dismiss()
            }
        }
    }

    override fun setup() {

        orderViewModel.previousSelectedProduct?.let {
            editMode = true
            binding.apply {
                btnAddProduct.text = "Replace"
                etQuantity.setText(it.quantity.toString())
            }

        }



        productAdapter = ProductPagingAdapter {
            orderViewModel.selectedProduct.value = it
        }


        lifecycleScope.launchWhenCreated {
            productViewModel.productItemList.collectLatest {
                productAdapter.submitData(it)
            }
        }

        orderViewModel.selectedProduct.observe(viewLifecycleOwner){product ->
            if(product != null){
                with(binding){
                    tvSelectedName.text = product.name
                    tvSelectedPrice.text = Utils.formatMoneyVND(product.price)
                    Glide.with(binding.root)
                        .load(product.image)
                        .placeholder(R.drawable.logo)
                        .into(ivSelectedProduct)
                }
            }
        }


        binding.apply {
            rvProduct.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = productAdapter
            }
            btnBackDetail.setOnClickListener {
                findNavController().popBackStack()
            }
            btnAddProduct.setOnClickListener {
                if(validate()){
                    addToOrder()
                }

            }
        }
    }

    private fun validate(): Boolean {
        if(orderViewModel.selectedProduct.value == null){
            notiDialog.changeToFail("Please select a product")
            notiDialog.show()
            return false
        }

        if(binding.etQuantity.text.isBlank()){
            notiDialog.changeToFail("Please input a quantity")
            notiDialog.show()
            return false
        }


        return true
    }

    private fun addToOrder() {
        val quantity = binding.etQuantity.text.toString().toInt()
        if(quantity <= 0){
            notiDialog.changeToFail("Quantity should be more than zero")
            notiDialog.show()
            return
        }


        if(editMode){
            orderViewModel.changingProductOrder(quantity)
        }else{
            orderViewModel.addSelectedProductToOrder(quantity)
        }

        notiDialog.changeToSuccess("Add product to order successfully")
        notiDialog.show()
        notiDialog.setOnDismissListener{
            findNavController().popBackStack()
        }
        notiDialog.setOnCancelListener {
            findNavController().popBackStack()
        }


    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentAddProductOrderBinding
        get() = FragmentAddProductOrderBinding::inflate

}