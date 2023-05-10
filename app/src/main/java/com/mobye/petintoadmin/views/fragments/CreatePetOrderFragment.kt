package com.mobye.petintoadmin.views.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.mobye.petintoadmin.R
import com.mobye.petintoadmin.adapters.PetPagingAdapter
import com.mobye.petintoadmin.adapters.ProductItemAdapter
import com.mobye.petintoadmin.databinding.FragmentCreatePetOrderBinding
import com.mobye.petintoadmin.models.Order
import com.mobye.petintoadmin.models.apimodels.OrderCart
import com.mobye.petintoadmin.repositories.OrderRepository
import com.mobye.petintoadmin.utils.Constants
import com.mobye.petintoadmin.utils.Utils
import com.mobye.petintoadmin.viewmodels.AdminViewModelFactory
import com.mobye.petintoadmin.viewmodels.OrderViewModel
import com.mobye.petintoadmin.views.MainActivity
import com.mobye.petintoadmin.views.changeToFail
import com.mobye.petintoadmin.views.changeToSuccess


class CreatePetOrderFragment : BaseFragment<FragmentCreatePetOrderBinding>() {

    private val orderViewModel : OrderViewModel by activityViewModels {
        AdminViewModelFactory(OrderRepository())
    }


    private val loadingDialog : AlertDialog by lazy {(activity as MainActivity).loadingDialog}
    private val notiDialog : Dialog by lazy { Utils.createNotificationDialog(requireContext())}

    override fun setup() {
        (requireActivity() as MainActivity).hideNav()

        val statusAdapter = ArrayAdapter(
            requireActivity().baseContext,
            android.R.layout.simple_spinner_dropdown_item,
            Constants.orderStatus
        )

        orderViewModel.previousSelectedPet.observe(viewLifecycleOwner){
            it?.let {
                binding.apply {
                    tvPetName.text = it.name
                    tvPetPrice.text = Utils.formatMoneyVND(it.price)
                    tvPetGender.text = it.gender
                    tvPetType.text = it.type
                    Glide.with(root)
                        .load(it.image)
                        .placeholder(R.drawable.logo)
                        .into(ivPet)
                }
            }
        }


        binding.apply {

            btnBackDetail.setOnClickListener {
                findNavController().popBackStack()
            }
            spOrderStatus.apply {
                adapter = statusAdapter
                setSelection(0)
            }
            btnAddPet.setOnClickListener {
                orderViewModel.selectedPet.value = null
                findNavController().navigate(CreatePetOrderFragmentDirections.actionCreatePetOrderFragmentToAddPetOrderFragment())
            }
            btnCreate.setOnClickListener {
                if(validate()){
                    sendCreateOrder()
                }
            }
        }


    }

    private fun validate(): Boolean = with(binding){
        return Utils.checkEditText(etAddress) && Utils.checkEditText(etCustomerName) &&
                Utils.checkEditText(etPayment) && Utils.checkEditText(etPhone) &&
                Utils.checkRadioGroup(rgDelievery, rbNo)
    }

    private fun sendCreateOrder() {
        if(orderViewModel.previousSelectedPet.value == null){
            notiDialog.changeToFail("This order has no pet!")
            notiDialog.show()
            return
        }


        loadingDialog.show()
        with(binding){
            val newOrder = Order().apply {
                address = etAddress.text.toString().trim()
                customerName = etCustomerName.text.toString().trim()
                phone = etPhone.text.toString().trim()
                payment = etPayment.text.toString().trim()
                note = etNote.text.toString().trim()
                status = spOrderStatus.selectedItem.toString()
                isdelivery = if (rbYes.isChecked) "yes" else "no"
            }

            orderViewModel.createPetOrder(newOrder)

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

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentCreatePetOrderBinding
        get() = FragmentCreatePetOrderBinding::inflate
}