package com.mobye.petintoadmin.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mobye.petintoadmin.R
import com.mobye.petintoadmin.databinding.FragmentFilterBinding
import com.mobye.petintoadmin.repositories.OrderRepository
import com.mobye.petintoadmin.utils.Constants
import com.mobye.petintoadmin.utils.Utils
import com.mobye.petintoadmin.viewmodels.AdminViewModelFactory
import com.mobye.petintoadmin.viewmodels.OrderViewModel
import com.mobye.petintoadmin.views.MainActivity


class FilterFragment : BaseFragment<FragmentFilterBinding>() {

    private val orderViewModel : OrderViewModel by activityViewModels {
        AdminViewModelFactory(OrderRepository())
    }

    private var filterProduct = true



    private val args : FilterFragmentArgs by navArgs()
    override fun setup() {
        if(args.type == "Pet"){
            filterProduct = false
        }


        (requireActivity() as MainActivity).hideNav()

        val statusAdapter = ArrayAdapter<String>(
            requireActivity().baseContext,
            android.R.layout.simple_spinner_dropdown_item,
            Constants.orderStatus
        )

        val fromDatePicker = Utils.createSingleDatePicker("From date",{ formatted, date ->
            binding.tvDayFrom.text = formatted
        },"MM/dd/yyyy")

        val toDatePicker = Utils.createSingleDatePicker("To date",{ formatted, date ->
            binding.tvDayTo.text = formatted
        },"MM/dd/yyyy")

        binding.apply {
            trackOrderSpinner.apply {
                adapter = statusAdapter
                setSelection(0)
            }

            tvDayFrom.setOnClickListener {
                fromDatePicker.show(parentFragmentManager, "FROM_DATE_PICKER")
            }

            tvDayTo.setOnClickListener {
                toDatePicker.show(parentFragmentManager, "TO_DATE_PICKER")
            }

            btnSearch.setOnClickListener {
                applyFilter()
            }

            btnClear.setOnClickListener {
                clearFilter()
            }
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }


        }



        fillFilter()

    }

    private fun clearFilter() {
        if(filterProduct){
            orderViewModel.clearFilter()
        }else{
            orderViewModel.clearFilterPet()
        }
    }

    private fun applyFilter() {
        with(binding){
            if(filterProduct){
                orderViewModel.filterOrder(
                    tvDayFrom.text.toString().trim(),
                    tvDayTo.text.toString().trim(),
                    trackOrderSpinner.selectedItem.toString()
                )
            }else{
                orderViewModel.filterPetOrder(
                    tvDayFrom.text.toString().trim(),
                    tvDayTo.text.toString().trim(),
                    trackOrderSpinner.selectedItem.toString()
                )
            }
            findNavController().popBackStack()
        }
    }

    private fun fillFilter() {

    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentFilterBinding
        get() = FragmentFilterBinding::inflate

}