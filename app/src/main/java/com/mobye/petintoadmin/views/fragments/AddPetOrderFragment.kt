package com.mobye.petintoadmin.views.fragments

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.mobye.petintoadmin.R
import com.mobye.petintoadmin.adapters.PetPagingAdapter
import com.mobye.petintoadmin.databinding.FragmentAddPetOrderBinding
import com.mobye.petintoadmin.repositories.OrderRepository
import com.mobye.petintoadmin.repositories.ProductRepository
import com.mobye.petintoadmin.utils.Utils
import com.mobye.petintoadmin.viewmodels.AdminViewModelFactory
import com.mobye.petintoadmin.viewmodels.OrderViewModel
import com.mobye.petintoadmin.viewmodels.ProductViewModel
import com.mobye.petintoadmin.views.changeToFail
import com.mobye.petintoadmin.views.changeToSuccess
import kotlinx.coroutines.flow.collectLatest


class AddPetOrderFragment : BaseFragment<FragmentAddPetOrderBinding>() {

    private val productViewModel : ProductViewModel by activityViewModels {
        AdminViewModelFactory(ProductRepository())
    }
    private val orderViewModel : OrderViewModel by activityViewModels {
        AdminViewModelFactory(OrderRepository())
    }
    private lateinit var petAdapter : PetPagingAdapter


    private val notiDialog : Dialog by lazy {Utils.createNotificationDialog(requireContext())}

    override fun setup() {

        orderViewModel.previousSelectedPet.value?.let {pet ->
            binding.apply {
                btnAddProduct.text = "Replace"
                tvSelectedName.text = pet.name
                tvSelectedPrice.text = Utils.formatMoneyVND(pet.price)
                Glide.with(root)
                    .load(pet.image)
                    .placeholder(R.drawable.logo)
                    .into(ivSelectedPet)
            }

        }


        petAdapter = PetPagingAdapter {
           orderViewModel.selectedPet.value = it
        }

        lifecycleScope.launchWhenCreated {
            productViewModel.petItemList.collectLatest {
                petAdapter.submitData(it)
            }
        }

        orderViewModel.selectedPet.observe(viewLifecycleOwner){pet ->
            if(pet != null){
                with(binding){
                    tvSelectedName.text = pet.name
                    tvSelectedPrice.text = Utils.formatMoneyVND(pet.price)
                    Glide.with(root)
                        .load(pet.image)
                        .placeholder(R.drawable.logo)
                        .into(ivSelectedPet)
                }
            }
        }

        binding.apply {
            rvPet.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = petAdapter
            }
            btnBackDetail.setOnClickListener {
                findNavController().popBackStack()
            }
            btnAddProduct.setOnClickListener {
                if(validate()){
                    addToOrder()
                }

            }
        }

    }

    private fun addToOrder() {

        orderViewModel.addPetToOrder()

        notiDialog.changeToSuccess("Add pet to order successfully")
        notiDialog.show()
        notiDialog.setOnDismissListener{
            findNavController().popBackStack()
        }
        notiDialog.setOnCancelListener {
            findNavController().popBackStack()
        }
    }

    private fun validate(): Boolean {
        if(orderViewModel.selectedPet.value == null){
            notiDialog.changeToFail("Please select a pet")
            notiDialog.show()
            return false
        }

        return true
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentAddPetOrderBinding
        get() = FragmentAddPetOrderBinding::inflate

}