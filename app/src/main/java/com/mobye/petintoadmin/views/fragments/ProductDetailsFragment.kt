package com.mobye.petintoadmin.views.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.mobye.petintoadmin.R
import com.mobye.petintoadmin.databinding.FragmentProductDetailsBinding
import com.mobye.petintoadmin.repositories.ProductRepository
import com.mobye.petintoadmin.viewmodels.AdminViewModelFactory
import com.mobye.petintoadmin.viewmodels.ProductViewModel
import com.mobye.petintoadmin.views.MainActivity

class ProductDetailsFragment : BaseFragment<FragmentProductDetailsBinding>() {

    private val args : ProductDetailsFragmentArgs by navArgs()
    private val productViewModel : ProductViewModel by activityViewModels {
        AdminViewModelFactory(ProductRepository())
    }

    private val loadingDialog : AlertDialog by lazy {
        val activity = requireActivity() as MainActivity
        activity.loadingDialog
    }
    private val notiDialog : Dialog by lazy {
        val activity = requireActivity() as MainActivity
        activity.notiDialog
    }



    override fun setup() {
        fillFields()

        binding.apply {
            btnUpdate.setOnClickListener {
                if(validated()){
                    sendUpdateProduct()
                }
            }
            btnDelete.setOnClickListener {
                sendDeleteProduct()
            }
        }


    }

    private fun validated(): Boolean {
        return true
    }

    private fun sendDeleteProduct() {
        TODO("Not yet implemented")
    }

    private fun sendUpdateProduct() {
        TODO("Not yet implemented")
    }

    private fun fillFields() {
        TODO("Not yet implemented")
    }



    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentProductDetailsBinding
        get() = FragmentProductDetailsBinding::inflate

}