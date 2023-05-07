package com.mobye.petintoadmin.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.mobye.petintoadmin.R
import com.mobye.petintoadmin.adapters.OrderAdapter
import com.mobye.petintoadmin.databinding.FragmentOrderManagementBinding


class OrderManagementFragment : BaseFragment<FragmentOrderManagementBinding>() {

    private lateinit var viewPagerAdapter : OrderAdapter

    override fun setup() {

        viewPagerAdapter = OrderAdapter(this)

        val tabLayoutMediator =
            TabLayoutMediator(binding.orderTabLayout, binding.orderViewPager
            ) { tab, position ->
                when (position) {
                    0 -> tab.text = "Product"
                    1 -> tab.text = "Pet"
                }
            }

        binding.apply {
            orderViewPager.adapter = viewPagerAdapter
        }

        tabLayoutMediator.attach()

    }


    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentOrderManagementBinding
        get() = FragmentOrderManagementBinding::inflate
}