package com.mobye.petintoadmin.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.mobye.petintoadmin.R
import com.mobye.petintoadmin.adapters.OrderAdapter
import com.mobye.petintoadmin.adapters.OrderPagingAdapter
import com.mobye.petintoadmin.adapters.ProductPagingAdapter
import com.mobye.petintoadmin.databinding.FragmentProductOrderBinding
import com.mobye.petintoadmin.repositories.OrderRepository
import com.mobye.petintoadmin.repositories.ProductRepository
import com.mobye.petintoadmin.viewmodels.AdminViewModelFactory
import com.mobye.petintoadmin.viewmodels.OrderViewModel
import com.mobye.petintoadmin.viewmodels.ProductViewModel
import com.mobye.petintoadmin.views.MainActivity


class ProductOrderFragment : BaseFragment<FragmentProductOrderBinding>() {

    private val orderViewModel : OrderViewModel by activityViewModels {
        AdminViewModelFactory(OrderRepository())
    }
    private lateinit var orderAdapter: OrderPagingAdapter


    override fun setup() {
        (requireActivity() as MainActivity).showNav()


    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentProductOrderBinding
        get() = FragmentProductOrderBinding::inflate

}