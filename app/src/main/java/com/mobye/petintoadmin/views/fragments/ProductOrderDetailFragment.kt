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
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobye.petintoadmin.R
import com.mobye.petintoadmin.adapters.ProductItemAdapter
import com.mobye.petintoadmin.databinding.FragmentProductOrderDetailBinding
import com.mobye.petintoadmin.models.Order
import com.mobye.petintoadmin.repositories.OrderRepository
import com.mobye.petintoadmin.repositories.ProductRepository
import com.mobye.petintoadmin.utils.Constants.Companion.orderStatus
import com.mobye.petintoadmin.utils.Utils
import com.mobye.petintoadmin.utils.Utils.Companion.checkEditText
import com.mobye.petintoadmin.utils.Utils.Companion.checkRadioGroup
import com.mobye.petintoadmin.viewmodels.AdminViewModelFactory
import com.mobye.petintoadmin.viewmodels.OrderViewModel
import com.mobye.petintoadmin.viewmodels.ProductViewModel
import com.mobye.petintoadmin.views.MainActivity
import com.mobye.petintoadmin.views.changeToFail
import com.mobye.petintoadmin.views.changeToSuccess


class ProductOrderDetailFragment : BaseFragment<FragmentProductOrderDetailBinding>() {

    private val args : ProductOrderDetailFragmentArgs by navArgs()
    private val orderViewModel : OrderViewModel by activityViewModels {
        AdminViewModelFactory(OrderRepository())
    }

    private val productAdapter : ProductItemAdapter by lazy { ProductItemAdapter{} }

    private val loadingDialog : AlertDialog by lazy { Utils.getLoadingDialog(requireActivity()) }
    private val notiDialog : Dialog by lazy { Utils.createNotificationDialog(requireContext()) }
    private val warningDeleteDialog : AlertDialog by lazy {
        Utils.createConfirmDialog(requireContext(),"Delete","Delete this order ?") {
            sendDeleteOrder()
        }
    }

    private var currentStatus : String = ""



    override fun setup() {
        //ẩn thanh nav
        (requireActivity() as MainActivity).hideNav()

        val statusAdapter = ArrayAdapter<String>(
            requireActivity().baseContext,
            android.R.layout.simple_spinner_dropdown_item,
            orderStatus
        )

        orderViewModel.getOrderDetail(args.currentOrder.id)
        orderViewModel.productOrderList.observe(viewLifecycleOwner){

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
            spOrderStatus.adapter = statusAdapter
            btnUpdate.setOnClickListener {
                if(validate()){
                    sendUpdateOrder()
                }
            }
            btnDelete.setOnClickListener {
                warningDeleteDialog.show()
            }
        }

        fillFields()

    }


    private fun validate(): Boolean = with(binding){
        ((checkEditText(etAddress) && rbYes.isChecked) || rbNo.isChecked) && checkEditText(etCustomerName)
                && checkEditText(etPayment)
                && checkRadioGroup(rgDelievery,rbNo)
    }

    private fun sendUpdateOrder() {
        loadingDialog.show()
        with(binding){
            val updatedOrder = Order(
                id = args.currentOrder.id,
                address = etAddress.text.toString().trim(),
                customerName = etCustomerName.text.toString().trim(),
                phone = etPhone.text.toString().trim(),
                payment = etPayment.text.toString().trim(),
                note = etNote.text.toString().trim(),
                status = spOrderStatus.selectedItem.toString(),
                isdelivery = if(rbYes.isChecked) "yes" else "no",
                CustomerId = args.currentOrder.CustomerId
            )

            if(spOrderStatus.selectedItem.toString() == currentStatus){
                orderViewModel.updateOrder(updatedOrder)
            }else{
                orderViewModel.updateOrder(updatedOrder,true)
            }



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

    private fun sendDeleteOrder() {
        loadingDialog.show()
        val deletedOrder = Order(
            id = args.currentOrder.id
        )

        orderViewModel.deleteOrder(deletedOrder)
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

    private fun fillFields() {
        binding.apply {

            val order = args.currentOrder

            tvId.text = order.id
            etAddress.setText(order.address)
            etCustomerName.setText(order.customerName)
            etPhone.setText(order.phone)
            etNote.setText(order.note)
            etPayment.setText(order.payment)
            if(order.isdelivery == "yes"){
                rbYes.isChecked = true
            }else{
                rbNo.isChecked = true
            }
            tvTotal.text = Utils.formatMoneyVND(order.total)
            tvAmount.text = order.amount.toString()
            tvOrderDate.text = Utils.formatToLocalDate(order.orderDate)
            tvDateDelivery.text = Utils.formatToLocalDate(order.dateDelivery)
            spOrderStatus.setSelection(Utils.getIndexOrderStatus(order.status))
            currentStatus = order.status


        }
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentProductOrderDetailBinding
        get() = FragmentProductOrderDetailBinding::inflate

}