package com.mobye.petintoadmin.views.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import androidx.core.view.get
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.mobye.petintoadmin.R
import com.mobye.petintoadmin.databinding.FragmentBookingDetailsBinding
import com.mobye.petintoadmin.models.Booking
import com.mobye.petintoadmin.models.Product
import com.mobye.petintoadmin.repositories.BookingRepository
import com.mobye.petintoadmin.repositories.ProductRepository
import com.mobye.petintoadmin.utils.Constants
import com.mobye.petintoadmin.utils.Utils
import com.mobye.petintoadmin.viewmodels.AdminViewModelFactory
import com.mobye.petintoadmin.viewmodels.BookingViewModel
import com.mobye.petintoadmin.viewmodels.ProductViewModel
import com.mobye.petintoadmin.views.MainActivity
import com.mobye.petintoadmin.views.changeToFail
import com.mobye.petintoadmin.views.changeToSuccess

class BookingDetailsFragment : BaseFragment<FragmentBookingDetailsBinding>() {

    private val args : BookingDetailsFragmentArgs by navArgs()
    private val bookingViewModel : BookingViewModel by activityViewModels {
        AdminViewModelFactory(BookingRepository())
    }

    private var isHotel = true
    private lateinit var typeAdapter : ArrayAdapter<String>

    private val loadingDialog : AlertDialog by lazy {(activity as MainActivity).loadingDialog}
    private val notiDialog : Dialog by lazy { Utils.createNotificationDialog(requireContext())}

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentBookingDetailsBinding
        get() = FragmentBookingDetailsBinding::inflate

    override fun setup() {
        //ẩn thanh nav
        (requireActivity() as MainActivity).hideNav()

        val statusAdapter = ArrayAdapter<String>(
            requireActivity().baseContext,
            android.R.layout.simple_spinner_dropdown_item,
            Constants.statusBooking
        )

        val timeAdapterSpa = ArrayAdapter(
            requireActivity().baseContext,
            android.R.layout.simple_spinner_dropdown_item,
            Constants.timeSpa
        )




        val warningDeleteDialog = requireActivity().let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setMessage("Do you really want to delete this item?")
                setTitle("Delete")
                setPositiveButton("Yes") { _, _ ->
                    sendDeleteBooking()
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
                    sendUpdateBooking()
                }
            }
            btnDelete.setOnClickListener {
                warningDeleteDialog.show()

            }
            btnBackDetail.setOnClickListener {
                findNavController().popBackStack()
            }
            spTime.apply {
                adapter = timeAdapterSpa
            }

            statusBookingSpinner.apply {
                adapter = statusAdapter
            }

        }

        fillFields()
    }

    private fun fillFields() {
        with(binding){
            val booking = args.currentBooking

            tvService.text = booking.service
            etCustomerName.setText(booking.customerName)
            etPhone.setText(booking.phone)
            etPetName.setText(booking.petName)
            etGenre.setText(booking.genre)
            etWeight.setText(booking.weight)
            etCharge.setText(booking.charge.toString())
            statusBookingSpinner.setSelection(Utils.getIndexBookingStatus(booking.status))

            if(booking.service == "Spa"){
                isHotel =false
                layoutCheckOut.visibility = View.GONE
                layoutTime.visibility = View.VISIBLE
                typeAdapter = ArrayAdapter(
                    requireActivity().baseContext,
                    android.R.layout.simple_spinner_dropdown_item,
                    Constants.typeBookingSpa
                )
                typeBookingSpinner.adapter = typeAdapter
                typeBookingSpinner.setSelection(Utils.getIndexBookingTypeSpa(booking.type))
                bookingViewModel.checkIn = Utils.getDateFromString(booking.checkIn)
                etCheckIn.setText(Utils.formatToLocalDate(booking.checkIn,"dd/MM/yyyy"))


                spTime.setSelection(Utils.getIndexTimeSpa(booking.checkIn.substringAfter('T')))
            }
            else{
                isHotel = true
                layoutCheckOut.visibility = View.VISIBLE
                layoutTime.visibility = View.GONE
                typeAdapter = ArrayAdapter(
                    requireActivity().baseContext,
                    android.R.layout.simple_spinner_dropdown_item,
                    Constants.typeBookingHotel
                )
                typeBookingSpinner.adapter = typeAdapter
                typeBookingSpinner.setSelection(Utils.getIndexBookingTypeHotel(booking.type))
                bookingViewModel.checkIn = Utils.getDateFromString(booking.checkIn)
                etCheckIn.setText(Utils.formatToLocalDate(booking.checkIn,"dd/MM/yyyy"))
                bookingViewModel.checkOut = Utils.getDateFromString(booking.checkOut)
                etCheckOut.setText(Utils.formatToLocalDate(booking.checkOut,"dd/MM/yyyy"))
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

        val validateDate = if(isHotel){
            checkEditText(etCheckIn) && checkEditText(etCheckOut)
        }else{
            checkEditText(etCheckIn)
        }

        return validatedFields && validateDate
    }

    private fun sendDeleteBooking() {
        loadingDialog.show()
        val deletedBooking = Booking(
            id = args.currentBooking.id
        )

        bookingViewModel.deleteBooking(deletedBooking)

        bookingViewModel.response.observe(viewLifecycleOwner){
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


            }
            else{
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

    private fun sendUpdateBooking() {
        loadingDialog.show()
        with(binding) {
                val updatedBooking  = Booking(
                    id = args.currentBooking.id,
                    customerName = etCustomerName.text.toString().trim(),
                    phone = etPhone.text.toString().trim(),
                    petName = etPetName.text.toString().trim(),
                    genre = etGenre.text.toString().trim(),
                    weight = etWeight.text.toString().trim(),
                    charge = etCharge.text.toString().toInt(),
                    status = statusBookingSpinner.selectedItem.toString(),
                    type = typeBookingSpinner.selectedItem.toString(),
                    service = args.currentBooking.service,
                    CustomerId = args.currentBooking.CustomerId,
            )

            if(isHotel){
                updatedBooking.apply {
                    checkIn = Utils.formatStandardTimeString(bookingViewModel.checkIn, "00:00:00")
                    checkOut = Utils.formatStandardTimeString(bookingViewModel.checkOut, "00:00:00")
                }
            }else{
                updatedBooking.apply {
                    checkIn = Utils.formatStandardTimeString(bookingViewModel.checkIn,
                        spTime.selectedItem.toString()
                    )
                }
            }


            bookingViewModel.updateBooking(updatedBooking)

            bookingViewModel.response.observe(viewLifecycleOwner){
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

}