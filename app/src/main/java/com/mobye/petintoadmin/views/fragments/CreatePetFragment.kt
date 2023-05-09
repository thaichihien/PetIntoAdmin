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
import androidx.navigation.fragment.findNavController
import com.mobye.petintoadmin.R
import com.mobye.petintoadmin.databinding.FragmentCreatePetBinding
import com.mobye.petintoadmin.databinding.FragmentCreateProductBinding
import com.mobye.petintoadmin.models.Pet
import com.mobye.petintoadmin.models.Product
import com.mobye.petintoadmin.repositories.ProductRepository
import com.mobye.petintoadmin.viewmodels.AdminViewModelFactory
import com.mobye.petintoadmin.viewmodels.ProductViewModel
import com.mobye.petintoadmin.views.MainActivity
import com.mobye.petintoadmin.views.changeToFail
import com.mobye.petintoadmin.views.changeToSuccess


class CreatePetFragment : BaseFragment<FragmentCreatePetBinding>() {


    private val productViewModel : ProductViewModel by activityViewModels {
        AdminViewModelFactory(ProductRepository())
    }

    //hai cửa sổ dùng để hiện loading và thông báo
    private val loadingDialog : AlertDialog by lazy {
        val activity = requireActivity() as MainActivity
        activity.loadingDialog
    }
    private val notiDialog : Dialog by lazy {
        Dialog(requireContext()).apply {
            setCancelable(true)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.notification_dialog)
            findViewById<Button>(R.id.btnClose).setOnClickListener{
                this.dismiss()
            }
        }
    }


    //Hàm này tương tự onViewCreated(), thực hiện các chức năng tại đây
    override fun setup() {

        //ẩn thanh nav
        (requireActivity() as MainActivity).hideNav()

        binding.apply {
            //Nút tạo
            btnCreate.setOnClickListener {
                if(validated()){    //Kiểm tra các ô không nhập trống
                    sendCreatePet() // gửi yêu cầu tạo
                }
            }

            //Nút quay lại
            btnBackDetail.setOnClickListener {
                findNavController().popBackStack()
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
                checkEditText(etType) && checkEditText(etWeight) &&
                checkEditText(etColor) && checkEditText(etVariety) &&
                checkEditText(etAge) && checkEditText(etVaccine) &&
                checkEditText(etImage)
    }


    private fun sendCreatePet()  {
        loadingDialog.show()        //mở cửa sở loading
        with(binding){   // dùng with để đỡ phải ghi binding nhiều lần
            var selectedID=rgGender.checkedRadioButtonId
            var inputGender="Male"
            if(selectedID==rbFemale.id)
                inputGender="Female"

            val newPet = Pet(
                name = etName.text.toString().trim(),
                price = etPrice.text.toString().toInt(),
                type = etType.text.toString().trim(),
                gender=inputGender.trim(),
                age = etAge.text.toString().toInt(),
                vaccine=etVaccine.text.toString().toInt(),
                image = etImage.text.toString().trim(),
                variety = etVariety.text.toString().trim(),
                weight = etWeight.text.toString().trim(),
                color=etColor.text.toString().trim()
            )


            //Gửi yêu cầu lên server tạo product
            productViewModel.createPet(newPet)

            //Hàm nhận kết quả
            productViewModel.response.observe(viewLifecycleOwner){
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



    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentCreatePetBinding
        get() = FragmentCreatePetBinding::inflate
}