package com.mobye.petintoadmin.views.fragments


import android.view.LayoutInflater

import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData

import androidx.recyclerview.widget.LinearLayoutManager

import com.mobye.petintoadmin.adapters.OrderPagingAdapter

import com.mobye.petintoadmin.databinding.FragmentProductOrderBinding
import com.mobye.petintoadmin.repositories.OrderRepository

import com.mobye.petintoadmin.viewmodels.AdminViewModelFactory
import com.mobye.petintoadmin.viewmodels.OrderViewModel

import com.mobye.petintoadmin.views.MainActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class ProductOrderFragment : BaseFragment<FragmentProductOrderBinding>() {

    private val orderViewModel : OrderViewModel by activityViewModels {
        AdminViewModelFactory(OrderRepository())
    }
    private lateinit var orderAdapter: OrderPagingAdapter


    override fun setup() {
        (requireActivity() as MainActivity).showNav()


        orderAdapter = OrderPagingAdapter {
            findNavController().navigate(OrderManagementFragmentDirections.actionOrderManagementFragmentToProductOrderDetailFragment(it))
        }


        lifecycleScope.launchWhenCreated {
            orderViewModel.orderItemList.collectLatest {
                orderAdapter.submitData(it)
                if(orderAdapter.itemCount <= 0){
                    Toast.makeText(requireContext(),"No results were found", Toast.LENGTH_SHORT).show()
                }
            }
        }



        binding.apply {


            rvOrder.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = orderAdapter
            }


            btnAdd.setOnClickListener {
                orderViewModel.emptyProductOrderList()
                findNavController().navigate(OrderManagementFragmentDirections.actionOrderManagementFragmentToCreateProductOrderFragment())
            }

            btnFilter.setOnClickListener{
                findNavController().navigate(OrderManagementFragmentDirections.actionOrderManagementFragmentToFilterFragment("product"))
            }

            btnRefresh.setOnClickListener {
                lifecycleScope.launch {
                    orderAdapter.submitData(PagingData.empty())
                    orderAdapter.notifyDataSetChanged()
                    orderViewModel.orderItemList.collectLatest {
                        orderAdapter.submitData(it)
                        if(orderAdapter.itemCount <= 0){
                            Toast.makeText(requireContext(),"No results were found", Toast.LENGTH_SHORT).show()
                        }
                    }

                }

            }
        }



    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentProductOrderBinding
        get() = FragmentProductOrderBinding::inflate

}