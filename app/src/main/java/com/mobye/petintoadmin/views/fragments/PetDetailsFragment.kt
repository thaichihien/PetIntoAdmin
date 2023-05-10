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
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.mobye.petintoadmin.R
import com.mobye.petintoadmin.databinding.FragmentPetDetailsBinding
import com.mobye.petintoadmin.models.Pet
import com.mobye.petintoadmin.repositories.ProductRepository
import com.mobye.petintoadmin.viewmodels.AdminViewModelFactory
import com.mobye.petintoadmin.viewmodels.ProductViewModel
import com.mobye.petintoadmin.views.MainActivity
import com.mobye.petintoadmin.views.changeToFail
import com.mobye.petintoadmin.views.changeToSuccess

class PetDetailsFragment : BaseFragment<FragmentPetDetailsBinding>() {

    private val args : PetDetailsFragmentArgs by navArgs()
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
        (requireActivity() as MainActivity).hideNav()

        fillFields()

        val warningDeleteDialog = requireActivity().let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setMessage("Do you really want to delete this item?")
                setTitle("Delete")
                setPositiveButton("Yes") { _, _ ->
                    sendDeletePet()
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
                    sendUpdatePet()
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
            val pet = args.currentPet

            tvId.text = pet.id
            etName.setText(pet.name)
            etPrice.setText(pet.price.toString())
            etType.setText(pet.type)
            etWeight.setText(pet.weight)
            etAge.setText(pet.age.toString())
            etColor.setText(pet.color)
            etVaccine.setText(pet.vaccine.toString())
            etVariety.setText(pet.variety)
            etImage.setText(pet.image)
            Glide.with(binding.root)
                .load(pet.image)
                .placeholder(R.drawable.logo)
                .into(ivPet)



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
                checkEditText(etAge) && checkEditText(etColor) &&
                checkEditText(etColor) && checkEditText(etVaccine) &&
                checkEditText(etVariety) && checkEditText(etImage)
    }

    private fun sendDeletePet() {
        loadingDialog.show()
        with(binding) {
            val deletedPet = Pet(
                id = tvId.text.toString()
            )

            productViewModel.deletePet(deletedPet)

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

    private fun sendUpdatePet() {
        loadingDialog.show()
        with(binding) {
            var selectedID=rgGender.checkedRadioButtonId
            var inputGender="Male"
            if(selectedID==rbFemale.id)
                inputGender="Female"

            val updatedPet = Pet(
                id = tvId.text.toString(),
                name = etName.text.toString().trim(),
                price = etPrice.text.toString().toInt(),
                type = etType.text.toString().trim(),
                image = etImage.text.toString().trim(),
                gender = inputGender,
                age = etAge.text.toString().toInt(),
                vaccine = etVaccine.text.toString().toInt(),
                variety = etVariety.text.toString().trim(),
                weight = etWeight.text.toString().trim(),
                color = etColor.text.toString().trim(),
            )

            productViewModel.updatePet(updatedPet)

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

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPetDetailsBinding
        get() = FragmentPetDetailsBinding::inflate

}