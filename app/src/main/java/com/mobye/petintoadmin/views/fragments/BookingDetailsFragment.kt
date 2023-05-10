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

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentBookingDetailsBinding
        get() = FragmentBookingDetailsBinding::inflate

    override fun setup() {
        //ẩn thanh nav
        (requireActivity() as MainActivity).hideNav()

        val statusAdapter = ArrayAdapter<String>(
            requireActivity().baseContext,
            android.R.layout.simple_spinner_dropdown_item,
            Constants.orderStatus
        )

        val typeAdapter = ArrayAdapter<String>(
            requireActivity().baseContext,
            android.R.layout.simple_spinner_dropdown_item,
            Constants.orderStatus
        )

        val serviceAdapter = ArrayAdapter<String>(
            requireActivity().baseContext,
            android.R.layout.simple_spinner_dropdown_item,
            Constants.orderStatus
        )

        fillFields()

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
            statusBookingSpinner.adapter = statusAdapter
            typeBookingSpinner.adapter = typeAdapter
            serviceBookingSpinner.adapter = serviceAdapter
        }
    }

    private fun fillFields() {
        with(binding){
            val booking = args.currentBooking

            etBookingCheckIn.setText(booking.checkIn.toString().trim())
            etOwnerName.setText(booking.customerName)
            etOwnerPhone.setText(booking.phone)
            etPetName.setText(booking.petName)
            etPetGender.setText(booking.genre)
            etPetWeight.setText(booking.weight)
            statusBookingSpinner.setSelection(Utils.getIndexBookingStatus(booking.status))

            if(booking.service == "Spa"){
                tvBookingCheckOut.visibility = View.GONE
                etBookingCheckOut.visibility = View.GONE
                serviceBookingSpinner.setSelection(Utils.getIndexService(booking.service))
                typeBookingSpinner.setSelection(Utils.getIndexBookingTypeSpa(booking.type))
            }
            else{
                etBookingCheckOut.setText(booking.checkOut)
                serviceBookingSpinner.setSelection(Utils.getIndexService(booking.service))
                typeBookingSpinner.setSelection(Utils.getIndexBookingTypeHotel(booking.type))
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
        return checkEditText(etBookingCheckIn) &&
                checkEditText(etOwnerName) && checkEditText(etOwnerPhone) &&
                checkEditText(etPetName) && checkEditText(etPetGender) &&
                checkEditText(etPetWeight)
    }

    private fun sendDeleteBooking() {
        loadingDialog.show()
        with(binding) {
            val deletedBooking = Booking(
                id = idEdt.text.toString()
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
    }

    private fun sendUpdateBooking() {
        loadingDialog.show()
        with(binding) {
            val updatedBooking = Booking(
                id = idEdt.text.toString().trim(),
                checkIn = etBookingCheckIn.text.toString().trim(),
                service = serviceBookingSpinner.selectedItem.toString(),
                type = typeBookingSpinner.selectedItem.toString(),
                customerName = etOwnerName.text.toString().trim(),
                phone = etOwnerPhone.text.toString().trim(),
                petName = etPetName.text.toString().trim(),
                genre = etPetGender.text.toString().trim(),
                weight = etPetWeight.text.toString().trim(),
                status = statusBookingSpinner.selectedItem.toString()
            )

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