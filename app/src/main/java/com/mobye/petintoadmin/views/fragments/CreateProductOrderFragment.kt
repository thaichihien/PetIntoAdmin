package com.mobye.petintoadmin.views.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobye.petintoadmin.R
import com.mobye.petintoadmin.adapters.ProductItemAdapter
import com.mobye.petintoadmin.databinding.FragmentCreateProductOrderBinding
import com.mobye.petintoadmin.models.Order
import com.mobye.petintoadmin.models.apimodels.OrderCart
import com.mobye.petintoadmin.repositories.OrderRepository
import com.mobye.petintoadmin.utils.Constants
import com.mobye.petintoadmin.utils.Utils
import com.mobye.petintoadmin.utils.Utils.Companion.checkEditText
import com.mobye.petintoadmin.utils.Utils.Companion.checkRadioGroup
import com.mobye.petintoadmin.viewmodels.AdminViewModelFactory
import com.mobye.petintoadmin.viewmodels.OrderViewModel
import com.mobye.petintoadmin.views.MainActivity
import com.mobye.petintoadmin.views.changeToFail
import com.mobye.petintoadmin.views.changeToSuccess


class CreateProductOrderFragment : BaseFragment<FragmentCreateProductOrderBinding>() {

    private val orderViewModel : OrderViewModel by activityViewModels {
        AdminViewModelFactory(OrderRepository())
    }

    private lateinit var productAdapter : ProductItemAdapter

    private val loadingDialog : AlertDialog by lazy {(activity as MainActivity).loadingDialog}
    private val notiDialog : Dialog by lazy {Utils.createNotificationDialog(requireContext())}


    override fun setup() {

        (requireActivity() as MainActivity).hideNav()

        val statusAdapter = ArrayAdapter(
            requireActivity().baseContext,
            android.R.layout.simple_spinner_dropdown_item,
            Constants.orderStatus
        )





        productAdapter = ProductItemAdapter {
            orderViewModel.selectProduct(it)
            findNavController().navigate(
                CreateProductOrderFragmentDirections.
                actionCreateProductOrderFragmentToAddProductOrderFragment())
        }

        orderViewModel.productOrderList.observe(viewLifecycleOwner){
            Log.e("CreateOrder",it.size.toString())
            productAdapter.differ.submitList(it)
        }

        binding.apply {
            rvProductOrder.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = productAdapter
            }
            btnBackDetail.setOnClickListener {
                findNavController().popBackStack()
            }
            spOrderStatus.apply {
                adapter = statusAdapter
                setSelection(0)
            }
            btnAddProduct.setOnClickListener {
                orderViewModel.emptySelectedProduct()
                findNavController().navigate(
                    CreateProductOrderFragmentDirections.
                    actionCreateProductOrderFragmentToAddProductOrderFragment())
            }
            btnCreate.setOnClickListener {
                if(validate()){
                    sendCreateOrder()
                }
            }
        }
    }

    private fun validate(): Boolean = with(binding){
        return checkEditText(etAddress) && checkEditText(etCustomerName) &&
                checkEditText(etPayment) && checkEditText(etPhone) &&
                checkRadioGroup(rgDelievery,rbNo)
    }

    private fun sendCreateOrder() {
        if(orderViewModel.isProductOrderEmpty()){
            notiDialog.changeToFail("This order has no products!")
            notiDialog.show()
            return
        }


        loadingDialog.show()
        with(binding){
            val newOrder = OrderCart().apply {
                address = etAddress.text.toString().trim()
                customerName = etCustomerName.text.toString().trim()
                phone = etPhone.text.toString().trim()
                payment = etPayment.text.toString().trim()
                note = etNote.text.toString().trim()
                status = spOrderStatus.selectedItem.toString()
                isdelivery = if (rbYes.isChecked) "yes" else "no"
            }

            orderViewModel.createOrder(newOrder)

            orderViewModel.response.observe(viewLifecycleOwner){
                loadingDialog.dismiss()
                if(it.result){
                    notiDialog.changeToSuccess(it.reason)
                    notiDialog.setOnCancelListener{
                        findNavController().popBackStack()
                    }
                    notiDialog.setOnDismissListener{
                        findNavController().popBackStack()
                    }
                    notiDialog.show()

                }else{
                    notiDialog.changeToFail(it.reason)
                    notiDialog.setOnDismissListener(null)
                    notiDialog.setOnCancelListener(null)
                    notiDialog.show()
                }
            }
        }
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentCreateProductOrderBinding
        get() = FragmentCreateProductOrderBinding::inflate

}