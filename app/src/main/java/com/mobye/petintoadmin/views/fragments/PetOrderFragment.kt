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
import com.mobye.petintoadmin.adapters.OrderPagingAdapter
import com.mobye.petintoadmin.databinding.FragmentPetOrderBinding
import com.mobye.petintoadmin.repositories.OrderRepository
import com.mobye.petintoadmin.viewmodels.AdminViewModelFactory
import com.mobye.petintoadmin.viewmodels.OrderViewModel
import com.mobye.petintoadmin.views.MainActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class PetOrderFragment : BaseFragment<FragmentPetOrderBinding>() {


    private val orderViewModel : OrderViewModel by activityViewModels {
        AdminViewModelFactory(OrderRepository())
    }
    private lateinit var orderAdapter: OrderPagingAdapter

    override fun setup() {
        (requireActivity() as MainActivity).showNav()

        orderAdapter = OrderPagingAdapter {
            findNavController().navigate(OrderManagementFragmentDirections.actionOrderManagementFragmentToPetOrderDetailsFragment(it))
        }

        lifecycleScope.launchWhenCreated {
            orderViewModel.petOrderList.collectLatest {
                orderAdapter.submitData(it)
            }
        }



        binding.apply {
            rvPetOrder.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = orderAdapter
            }


            btnAdd.setOnClickListener {
                findNavController().navigate(OrderManagementFragmentDirections.actionOrderManagementFragmentToCreatePetOrderFragment())
            }

            btnRefresh.setOnClickListener {
                lifecycleScope.launch {
//                    orderAdapter.submitData(PagingData.empty())
//                    orderAdapter.notifyDataSetChanged()
                    orderAdapter.refresh()

                    //orderViewModel.filterOrder(binding.etSearchProduct.text.toString().trim())
                }

            }
        }

    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPetOrderBinding
        get() = FragmentPetOrderBinding::inflate

}