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
import com.mobye.petintoadmin.viewmodels.AdminViewModelFactory
import com.mobye.petintoadmin.viewmodels.BookingViewModel
import com.mobye.petintoadmin.viewmodels.ProductViewModel
import com.mobye.petintoadmin.views.MainActivity
import com.mobye.petintoadmin.views.changeToFail
import com.mobye.petintoadmin.views.changeToSuccess

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CreateBookingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateBookingFragment : BaseFragment<FragmentCreateBookingBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentCreateBookingBinding
        get() = FragmentCreateBookingBinding::inflate

    private val bookingViewModel : BookingViewModel by activityViewModels {
        AdminViewModelFactory(BookingRepository())
    }

    //hai cửa sổ dùng để hiện loading và thông báo
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

    var typeAdapter : ArrayAdapter<String?>? = null
    private var checkInPicker : MaterialDatePicker<Long>? = null
    private var checkOutPicker : MaterialDatePicker<Long>? = null

    override fun setup() {
        //ẩn thanh nav
        (requireActivity() as MainActivity).hideNav()

        val statusAdapter = ArrayAdapter(
            requireActivity().baseContext,
            android.R.layout.simple_spinner_dropdown_item,
            Constants.orderStatus
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

        binding.apply {
            checkInPicker = Utils.createSingleDatePicker("Check in", {
                checkInPicked = it
                etCheckIn.setText(it)
            }, "MM/dd/yyyy")

            checkOutPicker = Utils.createSingleDatePicker("Check out", {
                checkOutPicked = it
                etCheckOut.setText(it)
            }, "MM/dd/yyyy")

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

            etCheckIn.setOnClickListener{
                checkInPicker!!.show(parentFragmentManager, "DAY_PICKER")
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
                        tvCheckOut.visibility = View.VISIBLE
                        etCheckOut.visibility = View.VISIBLE
                    }
                    else{
                        typeBookingSpinner.apply {
                            adapter = typeAdapterSpa
                            setSelection(0)
                        }
                        tvCheckOut.visibility = View.GONE
                        etCheckOut.visibility = View.GONE
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
        return checkEditText(etCharge) &&
                checkEditText(etCheckIn) && checkEditText(etCheckOut) &&
                checkEditText(etCustomerName) && checkEditText(etGender) &&
                checkEditText(etPetName) && checkEditText(etPhone) && checkEditText(etWeight)
    }

    private var checkInPicked : String = ""
    private var checkOutPicked : String = ""


    private fun sendCreateBooking()  {
        loadingDialog.show()        //mở cửa sở loading
        with(binding){   // dùng with để đỡ phải ghi binding nhiều lần
            var newBooking : Booking? = null


            if(serviceBookingSpinner.selectedItem.toString() == "Spa"){
                newBooking = Booking(
                    service = serviceBookingSpinner.selectedItem.toString(),
                    charge = etCharge.text.toString().trim().toInt(),
                    type = typeBookingSpinner.selectedItem.toString(),
                    checkIn = checkInPicked,
                    customerName = etCustomerName.text.toString().trim(),
                    genre = etGender.text.toString().trim(),
                    petName = etPetName.text.toString().trim(),
                    phone = etPhone.text.toString().trim(),
                    weight = etWeight.text.toString().trim(),
                    status = spBookingStatus.selectedItem.toString()
                )
            }
            else{
                newBooking = Booking(
                    service = serviceBookingSpinner.selectedItem.toString(),
                    charge = etCharge.text.toString().trim().toInt(),
                    type = typeBookingSpinner.selectedItem.toString(),
                    checkIn = checkInPicked,
                    checkOut = checkOutPicked,
                    customerName = etCustomerName.text.toString().trim(),
                    genre = etGender.text.toString().trim(),
                    petName = etPetName.text.toString().trim(),
                    phone = etPhone.text.toString().trim(),
                    weight = etWeight.text.toString().trim(),
                    status = spBookingStatus.selectedItem.toString()
                )
            }



            //Gửi yêu cầu lên server tạo Booking
            bookingViewModel.createBooking(newBooking)

            //Hàm nhận kết quả
            bookingViewModel.response.observe(viewLifecycleOwner){
                loadingDialog.dismiss() // đã có kết quả, tắt loading
                if(it.result){      //result = true là thành công

                    //hiện thông báo thành công kèm nội dung thông báo ở reason
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


                    //Hai hàm bắt sự kiện đóng của sổ thông báo
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