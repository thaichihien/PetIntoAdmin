package com.mobye.petintoadmin.views.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.mobye.petintoadmin.R
import com.mobye.petintoadmin.databinding.FragmentCreateProductBinding
import com.mobye.petintoadmin.models.Product
import com.mobye.petintoadmin.repositories.ProductRepository
import com.mobye.petintoadmin.viewmodels.AdminViewModelFactory
import com.mobye.petintoadmin.viewmodels.ProductViewModel
import com.mobye.petintoadmin.views.MainActivity
import com.mobye.petintoadmin.views.changeToFail
import com.mobye.petintoadmin.views.changeToSuccess


class CreateProductFragment : BaseFragment<FragmentCreateProductBinding>() {


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

        binding.apply {
            btnCreate.setOnClickListener {
                if(validated()){
                    sendCreateProduct()
                }
            }
        }

    }

    private fun checkEditText(et : EditText) : Boolean{
        return if(et.text.isBlank()){
            et.error = "Please fill in"
            false
        }else{
            et.error = null
            true
        }
    }


    private fun validated(): Boolean = with(binding){
        return checkEditText(etName) && checkEditText(etPrice) &&
                checkEditText(etDetail) && checkEditText(etStock) &&
                checkEditText(etTypePet)
    }


    private fun sendCreateProduct()  {
        loadingDialog.show()
       with(binding){
           val newProduct = Product(
               name = etName.text.toString().trim(),
               price = etPrice.text.toString().toInt(),
               typePet = etTypePet.text.toString().trim(),
               detail = etDetail.text.toString().trim(),
               stock = etStock.text.toString().toInt(),
               image = etImage.text.toString().trim()
           )


           productViewModel.createProduct(newProduct)


           productViewModel.response.observe(viewLifecycleOwner){
                loadingDialog.dismiss()
                if(it.result){
                    notiDialog.changeToSuccess(it.reason)
                    notiDialog.show()
                    notiDialog.setOnCancelListener {
                        findNavController().popBackStack()
                    }

                }else{
                    notiDialog.changeToFail(it.reason)
                    notiDialog.show()
                }

           }
       }
    }



    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentCreateProductBinding
        get() = FragmentCreateProductBinding::inflate
}