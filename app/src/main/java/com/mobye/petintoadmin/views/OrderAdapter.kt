package com.mobye.petintoadmin.views

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mobye.petintoadmin.views.fragments.PetOrderFragment
import com.mobye.petintoadmin.views.fragments.ProductOrderFragment

class OrderAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> ProductOrderFragment()
            1-> PetOrderFragment()
            else -> {
                ProductOrderFragment()
            }
        }
    }

}