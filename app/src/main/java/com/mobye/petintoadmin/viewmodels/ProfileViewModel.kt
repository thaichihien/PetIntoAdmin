package com.mobye.petintoadmin.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.mobye.petintoadmin.models.Admin
import com.mobye.petintoadmin.models.apimodels.ApiResponse
import com.mobye.petintoadmin.repositories.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class ProfileViewModel(
    private val repository: ProfileRepository
) : ViewModel(){

    private val TAG = "ProfileViewModel"

    private var _response : MutableLiveData<ApiResponse<*>> = MutableLiveData()
    val response get() = _response

    val admin : MutableLiveData<Admin> by lazy { MutableLiveData() }
    val responseAPI : MutableLiveData<ApiResponse<Any>> by lazy { MutableLiveData() }

    fun getAdmin(id : String,token : String){
        // get user data by id from backend (id,email,name)
        viewModelScope.launch {
            try {
                val tokenSend = mapOf<String,String>(Pair("token",token))


                val response = repository.getAdmin(id,tokenSend)
                if(response.isSuccessful){
                    val res = response.body()!!
                    if(res.result){
                        admin.value = res.body
                    }else{

                    }

                }else{
                    //505 server error
                    responseAPI.value = ApiResponse.convertToAny(response.body()!!)
                    Log.e(TAG, response.body()!!.error)

                }

            }catch (e : SocketTimeoutException){
                Log.e(TAG,e.toString())
            }
            catch (e: Exception){
                // no internet connection
                Log.e(TAG,e.toString())
            }
        }

    }

    private val dumb : MutableStateFlow<String> by lazy { MutableStateFlow("") }

    val reportList = dumb.flatMapLatest {
        repository.getReportSource().cachedIn(viewModelScope)
    }


    fun deleteReport(id : String){
        try {
            viewModelScope.launch {
                _response.value = repository.deleteReport(id)
            }
        }catch (ex : Exception){
            Log.e(TAG,ex.toString())
        }
    }

}