package com.mobye.petintoadmin.views.fragments

import android.os.Bundle
import android.util.Log
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
import com.mobye.petintoadmin.adapters.ProductPagingAdapter
import com.mobye.petintoadmin.databinding.FragmentBookingManagementBinding
import com.mobye.petintoadmin.databinding.FragmentProductManagementBinding
import com.mobye.petintoadmin.repositories.ProductRepository
import com.mobye.petintoadmin.viewmodels.AdminViewModelFactory
import com.mobye.petintoadmin.viewmodels.ProductViewModel
import com.mobye.petintoadmin.views.MainActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


// Sử dụng kế thừa BaseFragment để đỡ tốn công set up, nhớ Thay đổi kiểu biến cho phù hợp
// và overide 2 hàm setup() và bindingInflater
class ProductManagementFragment : BaseFragment<FragmentProductManagementBinding>() {

    private val productViewModel : ProductViewModel by activityViewModels {
        AdminViewModelFactory(ProductRepository())
    }
    private lateinit var productAdapter : ProductPagingAdapter


    //Hàm này tương tự onViewCreated(), thực hiện các chức năng tại đây
    override fun setup() {

        //Hiện thanh nav
        (requireActivity() as MainActivity).showNav()


        //adapter phân trang
        productAdapter = ProductPagingAdapter {
            //khi nhấn vào item -> di chuyển đến detail
            findNavController().navigate(ProductManagementFragmentDirections.actionProductManagementFragmentToProductDetailsFragment(it))
        }


        //Gọi lấy dữ liệu
        lifecycleScope.launchWhenCreated {
            productViewModel.productItemList.collectLatest {
                productAdapter.submitData(it)
            }
        }


        //binding dữ liệu lấy được
        binding.apply {
            //Recycle view
            rvProduct.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = productAdapter
            }

            //nút thêm
            btnAdd.setOnClickListener {
                findNavController().navigate(ProductManagementFragmentDirections.actionProductManagementFragmentToCreateProductFragment())
            }

            btnRefresh.setOnClickListener{
                lifecycleScope.launch{
                    productAdapter.submitData(PagingData.empty())
                    productAdapter.notifyDataSetChanged()
                    productViewModel.searchProduct(binding.etSearchProduct.text.toString().trim())
                    productViewModel.productItemList.collectLatest {
                        productAdapter.submitData(it)
                    }
                }

            }




            etSearchProduct.setOnKeyListener { v, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {


                    lifecycleScope.launch{
                        productAdapter.submitData(PagingData.empty())
                        productAdapter.notifyDataSetChanged()

                        productViewModel.searchProduct(binding.etSearchProduct.text.toString().trim())
                    }
                    return@setOnKeyListener true
                }
                return@setOnKeyListener false

            }

        }


    }


    //Thay đổi kiểu biến cho phù hợp
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentProductManagementBinding
        get() = FragmentProductManagementBinding::inflate
}