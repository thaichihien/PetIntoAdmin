package com.mobye.petintoadmin.views.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.os.Message
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.mobye.petintoadmin.R
import com.mobye.petintoadmin.databinding.FragmentCreateBookingBinding
import com.mobye.petintoadmin.databinding.FragmentCreateProductBinding
import com.mobye.petintoadmin.models.Booking
import com.mobye.petintoadmin.models.Product
import com.mobye.petintoadmin.repositories.BookingRepository
import com.mobye.petintoadmin.repositories.ProductRepository
import com.mobye.petintoadmin.utils.Constants
import com.mobye.petintoadmin.utils.Utils
import com.mobye.petintoadmin.utils.Utils.Companion.checkRadioGroup
import com.mobye.petintoadmin.utils.Utils.Companion.checkTextView
import com.mobye.petintoadmin.viewmodels.AdminViewModelFactory
import com.mobye.petintoadmin.viewmodels.BookingViewModel
import com.mobye.petintoadmin.viewmodels.ProductViewModel
import com.mobye.petintoadmin.views.MainActivity
import com.mobye.petintoadmin.views.changeToFail
import com.mobye.petintoadmin.views.changeToSuccess
import java.text.SimpleDateFormat
import java.util.*

class CreateBookingFragment : BaseFragment<FragmentCreateBookingBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentCreateBookingBinding
        get() = FragmentCreateBookingBinding::inflate

    private val bookingViewModel : BookingViewModel by activityViewModels {
        AdminViewModelFactory(BookingRepository())
    }


    private val loadingDialog : AlertDialog by lazy {(activity as MainActivity).loadingDialog}
    private val notiDialog : Dialog by lazy { Utils.createNotificationDialog(requireContext())}



    override fun setup() {
        //ẩn thanh nav
        (requireActivity() as MainActivity).hideNav()

        val statusAdapter = ArrayAdapter(
            requireActivity().baseContext,
            android.R.layout.simple_spinner_dropdown_item,
            Constants.statusBooking
        )
        val serviceAdapter = ArrayAdapter(
            requireActivity().baseContext,
            android.R.layout.simple_spinner_dropdown_item,
            Constants.serviceBooking
        )

        val typeAdapterHotel = ArrayAdapter(
            requireActivity().baseContext,
            android.R.layout.simple_spinner_dropdown_item,
            Constants.typeBookingHotel
        )

        val typeAdapterSpa = ArrayAdapter(
            requireActivity().baseContext,
            android.R.layout.simple_spinner_dropdown_item,
            Constants.typeBookingSpa
        )

        val timeAdapterSpa = ArrayAdapter(
            requireActivity().baseContext,
            android.R.layout.simple_spinner_dropdown_item,
            Constants.timeSpa
        )

        val checkInPicker = Utils.createSingleDatePicker("From date",{ formatted,date ->
            binding.etCheckIn.setText(formatted)
            bookingViewModel.checkIn = date
        })

        val checkOutPicker = Utils.createSingleDatePicker("To date",{ formatted,date ->
            binding.etCheckOut.setText(formatted)
            bookingViewModel.checkOut = date
        })



        binding.apply {


            //Nút tạo
            btnCreate.setOnClickListener {
                if(validated()){    //Kiểm tra các ô không nhập trống
                    sendCreateBooking() // gửi yêu cầu tạo
                }
                else{
                    Log.d("CheckValidate", "NOT VALIDATED")
                }
            }

            //Nút quay lại
            btnBackDetail.setOnClickListener {
                findNavController().popBackStack()
            }

            spBookingStatus.apply {
                adapter = statusAdapter
                setSelection(0)
            }
            serviceBookingSpinner.apply {
                adapter = serviceAdapter
                setSelection(0)
            }
            spTime.apply {
                adapter = timeAdapterSpa
                setSelection(0)
            }

            etCheckIn.setOnClickListener{
                checkInPicker.show(parentFragmentManager, "FROM_DATE_PICKER")
            }

            etCheckOut.setOnClickListener{
                checkOutPicker.show(parentFragmentManager, "TO_DATE_PICKER")
            }





            serviceBookingSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    // Get the selected item from the adapter
                    val selectedItem = parent.getItemAtPosition(position) as String

                    if(selectedItem == "Hotel"){
                        typeBookingSpinner.apply {
                            adapter = typeAdapterHotel
                            setSelection(0)
                        }
                        layoutCheckOut.visibility = View.VISIBLE
                        layoutTime.visibility = View.GONE


                    }
                    else{
                        typeBookingSpinner.apply {
                            adapter = typeAdapterSpa
                            setSelection(0)
                        }
                       layoutCheckOut.visibility = View.GONE
                        layoutTime.visibility =View.VISIBLE
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Do nothing
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
        val validatedFields = checkEditText(etCharge) &&
                checkEditText(etCustomerName) && checkEditText(etGenre) &&
                checkEditText(etPetName) && checkEditText(etPhone) && checkEditText(etWeight)

        var validateDate = false
        validateDate = if(serviceBookingSpinner.selectedItemPosition == 0){
            checkTextView(etCheckIn,requireContext()) && checkTextView(etCheckOut,requireContext())
        }else{
            checkTextView(etCheckIn,requireContext())
        }

        return validatedFields && validateDate
    }

    private var checkInPicked : String = ""
    private var checkOutPicked : String = ""


    private fun sendCreateBooking()  {
        loadingDialog.show()
        with(binding){
            val newBooking = Booking(
                customerName = etCustomerName.text.toString().trim(),
                phone = etPhone.text.toString().trim(),
                petName = etPetName.text.toString().trim(),
                genre = etGenre.text.toString().trim(),
                weight = etWeight.text.toString().trim(),
                service = serviceBookingSpinner.selectedItem.toString(),
                charge = etCharge.text.toString().toInt(),
                status = spBookingStatus.selectedItem.toString(),
                type = typeBookingSpinner.selectedItem.toString()
            )

            // 0 == Hotel
            if(serviceBookingSpinner.selectedItemPosition == 0){
                newBooking.apply {
                    checkIn = Utils.formatStandardTimeString(bookingViewModel.checkIn, "00:00:00")
                    checkOut = Utils.formatStandardTimeString(bookingViewModel.checkOut, "00:00:00")
                }

            }else{
                newBooking.apply {
                    checkIn = Utils.formatStandardTimeString(bookingViewModel.checkIn,
                            spTime.selectedItem.toString()
                        )
                }
            }

            bookingViewModel.createBooking(newBooking)


            bookingViewModel.response.observe(viewLifecycleOwner){
                loadingDialog.dismiss()
                if(it.result){
                    notiDialog.changeToSuccess(it.reason)
                    //Hai hàm bắt sự kiện đóng của sổ thông báo
                    notiDialog.setOnDismissListener{
                        findNavController().popBackStack()  //quay về
                    }
                    notiDialog.setOnCancelListener {
                        findNavController().popBackStack() //quay về
                    }
                    notiDialog.show()



                }else{

                    //Hiện thông báo thất bại
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

}