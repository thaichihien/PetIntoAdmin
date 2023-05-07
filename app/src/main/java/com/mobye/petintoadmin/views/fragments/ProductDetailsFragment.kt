package com.mobye.petintoadmin.views.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.mobye.petintoadmin.R
import com.mobye.petintoadmin.databinding.FragmentProductDetailsBinding
import com.mobye.petintoadmin.models.Product
import com.mobye.petintoadmin.repositories.ProductRepository
import com.mobye.petintoadmin.viewmodels.AdminViewModelFactory
import com.mobye.petintoadmin.viewmodels.ProductViewModel
import com.mobye.petintoadmin.views.MainActivity
import com.mobye.petintoadmin.views.changeToFail
import com.mobye.petintoadmin.views.changeToSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductDetailsFragment : BaseFragment<FragmentProductDetailsBinding>() {

    private val args : ProductDetailsFragmentArgs by navArgs()
    private val productViewModel : ProductViewModel by activityViewModels {
        AdminViewModelFactory(ProductRepository())
    }

    private val loadingDialog : AlertDialog by lazy {
        val activity = requireActivity() as MainActivity
        activity.loadingDialog
    }
    val notiDialog : Dialog by lazy {
        Dialog(requireContext()).apply {
            setCancelable(true)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.notification_dialog)
            findViewById<Button>(R.id.btnClose).setOnClickListener{
                this.dismiss()
            }
        }
    }



    override fun setup() {

        //ẩn thanh nav
        (requireActivity() as MainActivity).hideNav()

        fillFields()

        val warningDeleteDialog = requireActivity().let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setMessage("Do you really want to delete this item?")
                setTitle("Delete")
                setPositiveButton("Yes") { _, _ ->
                    sendDeleteProduct()
                }
                setNegativeButton("No") { _, _ ->
                    //nothing
                }
                builder.create()
            }
        }

        binding.apply {
            btnUpdate.setOnClickListener {
                if(validated()){
                    sendUpdateProduct()
                }
            }
            btnDelete.setOnClickListener {
                warningDeleteDialog.show()

            }
            btnBackDetail.setOnClickListener {
                findNavController().popBackStack()
            }
        }


    }

    private fun fillFields() {
        with(binding){
            val product = args.currentProduct

            tvId.text = product.id
            etName.setText(product.name)
            etPrice.setText(product.price.toString())
            etDetail.setText(product.detail)
            etStock.setText(product.stock.toString())
            etTypePet.setText(product.typePet)
            etImage.setText(product.image)
            Glide.with(binding.root)
                .load(product.image)
                .placeholder(R.drawable.logo)
                .into(ivProduct)
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

    private fun sendDeleteProduct() {
        loadingDialog.show()
        with(binding) {
            val deletedProduct = Product(
                id = tvId.text.toString()
            )

            productViewModel.deleteProduct(deletedProduct)

            productViewModel.response.observe(viewLifecycleOwner){
                loadingDialog.dismiss()
                if(it.result){
                    notiDialog.changeToSuccess(it.reason)
                    notiDialog.setOnDismissListener{
                        findNavController().popBackStack()  //quay về
                    }
                    notiDialog.setOnCancelListener {
                        findNavController().popBackStack() //quay về
                    }
                    notiDialog.show()


                }else{
                    notiDialog.changeToFail(it.reason)
                    notiDialog.setOnDismissListener{
                        //nothing
                    }
                    notiDialog.setOnCancelListener {
                       //nothing
                    }
                    notiDialog.show()
                }
            }
        }
    }

    private fun sendUpdateProduct() {
        loadingDialog.show()
        with(binding) {
            val updatedProduct = Product(
                id = tvId.text.toString(),
                name = etName.text.toString().trim(),
                price = etPrice.text.toString().toInt(),
                typePet = etTypePet.text.toString().trim(),
                detail = etDetail.text.toString().trim(),
                stock = etStock.text.toString().toInt(),
                image = etImage.text.toString().trim()
            )

            productViewModel.updateProduct(updatedProduct)

            productViewModel.response.observe(viewLifecycleOwner){
                loadingDialog.dismiss()
                if(it.result){

                    notiDialog.changeToSuccess(it.reason)
                    notiDialog.setOnCancelListener(null)
                    notiDialog.setOnDismissListener(null)
                    notiDialog.setOnDismissListener{
                        findNavController().popBackStack()  //quay về
                    }
                    notiDialog.setOnCancelListener {
                        findNavController().popBackStack() //quay về
                    }
                    notiDialog.show()


                }else{
                    notiDialog.changeToFail(it.reason)
                    notiDialog.setOnCancelListener(null)
                    notiDialog.setOnDismissListener(null)
                    notiDialog.setOnDismissListener{
                       //nothing
                    }
                    notiDialog.setOnCancelListener {
                        //nothing
                    }
                    notiDialog.show()
                }


            }
        }
    }








    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentProductDetailsBinding
        get() = FragmentProductDetailsBinding::inflate

}