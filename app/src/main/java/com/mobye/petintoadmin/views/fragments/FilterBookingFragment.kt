package com.mobye.petintoadmin.views.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.mobye.petintoadmin.R
import com.mobye.petintoadmin.databinding.FragmentFilterBookingBinding
import com.mobye.petintoadmin.repositories.BookingRepository
import com.mobye.petintoadmin.utils.Constants
import com.mobye.petintoadmin.utils.Utils
import com.mobye.petintoadmin.viewmodels.AdminViewModelFactory
import com.mobye.petintoadmin.viewmodels.BookingViewModel
import com.mobye.petintoadmin.views.MainActivity


class FilterBookingFragment : BaseFragment<FragmentFilterBookingBinding>() {

    private val bookingViewModel : BookingViewModel by activityViewModels {
        AdminViewModelFactory(BookingRepository())
    }

    override fun setup() {
        (requireActivity() as MainActivity).hideNav()

        val statusAdapter = ArrayAdapter<String>(
            requireActivity().baseContext,
            android.R.layout.simple_spinner_dropdown_item,
            Constants.statusBooking
        )

        val fromDatePicker = Utils.createSingleDatePicker("From date", { formatted, date ->
            binding.tvDayFrom.text = formatted
        }, "MM/dd/yyyy",false)

        val toDatePicker = Utils.createSingleDatePicker("To date", { formatted, date ->
            binding.tvDayTo.text = formatted
        }, "MM/dd/yyyy",false)

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
        bookingViewModel.clearFilter()
        binding.apply {
            tvDayFrom.text = ""
            tvDayTo.text = ""
            trackOrderSpinner.setSelection(0)
        }
    }

    private fun applyFilter() {
        with(binding){
            val status = if(trackOrderSpinner.selectedItemPosition == 0){
                ""
            }else{
                trackOrderSpinner.selectedItem.toString()
            }



            bookingViewModel.filterBooking(
                tvDayFrom.text.toString(),
                tvDayTo.text.toString(),
                status
            )
            findNavController().popBackStack()
        }
    }

    private fun fillFilter() {
        binding.apply {
            tvDayFrom.text = bookingViewModel.fromDate.value
            tvDayTo.text = bookingViewModel.toDate
            trackOrderSpinner.setSelection(Utils.getIndexBookingStatus(bookingViewModel.statusQuery))
        }
    }



    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentFilterBookingBinding
        get() = FragmentFilterBookingBinding::inflate

}