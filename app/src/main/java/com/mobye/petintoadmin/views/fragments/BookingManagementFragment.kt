package com.mobye.petintoadmin.views.fragments

import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobye.petintoadmin.R
import com.mobye.petintoadmin.adapters.BookingPagingAdapter
import com.mobye.petintoadmin.adapters.ProductPagingAdapter
import com.mobye.petintoadmin.databinding.FragmentBookingDetailsBinding
import com.mobye.petintoadmin.databinding.FragmentBookingManagementBinding
import com.mobye.petintoadmin.databinding.FragmentProductManagementBinding
import com.mobye.petintoadmin.repositories.BookingRepository
import com.mobye.petintoadmin.repositories.ProductRepository
import com.mobye.petintoadmin.viewmodels.AdminViewModelFactory
import com.mobye.petintoadmin.viewmodels.BookingViewModel
import com.mobye.petintoadmin.viewmodels.ProductViewModel
import com.mobye.petintoadmin.views.MainActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class BookingManagementFragment : BaseFragment<FragmentBookingManagementBinding>() {

    private val bookingViewModel : BookingViewModel by activityViewModels {
        AdminViewModelFactory(BookingRepository())
    }
    private lateinit var bookingAdapter : BookingPagingAdapter

    override fun setup() {

        //Hiện thanh nav
        (requireActivity() as MainActivity).showNav()


        //adapter phân trang
        bookingAdapter = BookingPagingAdapter {
            //khi nhấn vào item -> di chuyển đến detail
            findNavController().navigate(BookingManagementFragmentDirections.actionBookingManagementFragmentToBookingDetailsFragment(it))
        }


        //Gọi lấy dữ liệu
        lifecycleScope.launchWhenCreated {
            bookingViewModel.bookingItemList.collectLatest {
                bookingAdapter.submitData(it)
            }
        }


        //binding dữ liệu lấy được
        binding.apply {
            //Recycle view
            rvBooking.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = bookingAdapter
            }

            //nút thêm
            btnAdd.setOnClickListener {
                findNavController().navigate(BookingManagementFragmentDirections.actionBookingManagementFragmentToCreateBookingFragment())
            }

            btnRefresh.setOnClickListener{
                bookingAdapter.refresh()
            }
        }


    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentBookingManagementBinding
        get() = FragmentBookingManagementBinding::inflate


}