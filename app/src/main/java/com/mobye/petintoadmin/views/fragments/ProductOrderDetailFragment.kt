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
import android.widget.Button
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobye.petintoadmin.R
import com.mobye.petintoadmin.adapters.ProductItemAdapter
import com.mobye.petintoadmin.databinding.FragmentProductOrderDetailBinding
import com.mobye.petintoadmin.repositories.OrderRepository
import com.mobye.petintoadmin.repositories.ProductRepository
import com.mobye.petintoadmin.utils.Utils
import com.mobye.petintoadmin.viewmodels.AdminViewModelFactory
import com.mobye.petintoadmin.viewmodels.OrderViewModel
import com.mobye.petintoadmin.viewmodels.ProductViewModel
import com.mobye.petintoadmin.views.MainActivity


class ProductOrderDetailFragment : BaseFragment<FragmentProductOrderDetailBinding>() {

    private val args : ProductOrderDetailFragmentArgs by navArgs()
    private val orderViewModel : OrderViewModel by activityViewModels {
        AdminViewModelFactory(OrderRepository())
    }

    private val productAdapter : ProductItemAdapter by lazy { ProductItemAdapter() }

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

        orderViewModel.getOrderDetail(args.currentOrder.id)
        orderViewModel.productOrderList.observe(viewLifecycleOwner){
            Log.e("ProductOrderdetail",it.size.toString())
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
            if(order.payment == "yes"){
                rbYes.isChecked = true
            }else{
                rbNo.isChecked = true
            }
            tvTotal.text = Utils.formatMoneyVND(order.total)
            tvAmount.text = order.amount.toString()
            tvOrderDate.text = Utils.formatToLocalDate(order.orderDate)

        }
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentProductOrderDetailBinding
        get() = FragmentProductOrderDetailBinding::inflate

}