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
import com.mobye.petintoadmin.adapters.PetPagingAdapter
import com.mobye.petintoadmin.databinding.FragmentPetManagmentBinding
import com.mobye.petintoadmin.repositories.ProductRepository
import com.mobye.petintoadmin.viewmodels.AdminViewModelFactory
import com.mobye.petintoadmin.viewmodels.ProductViewModel
import com.mobye.petintoadmin.views.MainActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PetManagmentFragment : BaseFragment<FragmentPetManagmentBinding>() {

    private val productViewModel : ProductViewModel by activityViewModels {
        AdminViewModelFactory(ProductRepository())
    }
    private lateinit var petAdapter : PetPagingAdapter

    override fun setup() {
        (requireActivity() as MainActivity).showNav()


        //adapter phân trang
        petAdapter = PetPagingAdapter {
            //khi nhấn vào item -> di chuyển đến detail
            findNavController().navigate(PetManagmentFragmentDirections.actionPetManagmentFragmentToPetDetailsFragment(it))
        }


        //Gọi lấy dữ liệu
        lifecycleScope.launchWhenCreated {
            productViewModel.petItemList.collectLatest {
                petAdapter.submitData(it)
            }
        }


        //binding dữ liệu lấy được
        binding.apply {
            //Recycle view
            rvPet.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = petAdapter
            }

            //nút thêm
            btnAdd.setOnClickListener {
                findNavController().navigate(PetManagmentFragmentDirections.actionPetManagmentFragmentToCreatePetFragment())
            }

            btnRefresh.setOnClickListener{
                lifecycleScope.launch{
                    petAdapter.submitData(PagingData.empty())
                    petAdapter.notifyDataSetChanged()

                    productViewModel.searchProduct(binding.etSearchPet.text.toString().trim())
                }

            }


            etSearchPet.setOnKeyListener { v, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {


                    lifecycleScope.launch{
                        petAdapter.submitData(PagingData.empty())
                        petAdapter.notifyDataSetChanged()

                        productViewModel.searchProduct(binding.etSearchPet.text.toString().trim())
                    }
                    return@setOnKeyListener true
                }
                return@setOnKeyListener false

            }

        }

    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPetManagmentBinding
        get() = FragmentPetManagmentBinding::inflate
}