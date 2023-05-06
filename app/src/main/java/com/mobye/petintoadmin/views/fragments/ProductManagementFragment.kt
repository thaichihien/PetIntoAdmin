package com.mobye.petintoadmin.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobye.petintoadmin.R
import com.mobye.petintoadmin.adapters.ProductPagingAdapter
import com.mobye.petintoadmin.databinding.FragmentBookingManagementBinding
import com.mobye.petintoadmin.databinding.FragmentProductManagementBinding
import com.mobye.petintoadmin.repositories.ProductRepository
import com.mobye.petintoadmin.viewmodels.AdminViewModelFactory
import com.mobye.petintoadmin.viewmodels.ProductViewModel
import kotlinx.coroutines.flow.collectLatest


class ProductManagementFragment : BaseFragment<FragmentProductManagementBinding>() {

    private val productViewModel : ProductViewModel by activityViewModels {
        AdminViewModelFactory(ProductRepository())
    }
    private lateinit var productAdapter : ProductPagingAdapter


    override fun setup() {
        productAdapter = ProductPagingAdapter {
            findNavController().navigate(ProductManagementFragmentDirections.actionProductManagementFragmentToProductDetailsFragment(it))
        }

        lifecycleScope.launchWhenCreated {
            productViewModel.productItemList.collectLatest {
                productAdapter.submitData(it)
            }
        }

        binding.apply {
            rvProduct.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = productAdapter
            }
            btnAdd.setOnClickListener {
                findNavController().navigate(ProductManagementFragmentDirections.actionProductManagementFragmentToCreateProductFragment())
            }

        }


    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentProductManagementBinding
        get() = FragmentProductManagementBinding::inflate
}