package com.mobye.petintoadmin.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.mobye.petintoadmin.models.Order
import com.mobye.petintoadmin.models.Pet
import com.mobye.petintoadmin.models.Product
import com.mobye.petintoadmin.models.apimodels.ApiResponse
import com.mobye.petintoadmin.models.apimodels.OrderCart
import com.mobye.petintoadmin.repositories.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class OrderViewModel(
    private val repository: OrderRepository
)  : ViewModel(){

    private val TAG = "OrderViewModel"

    private var _response : MutableLiveData<ApiResponse<*>> = MutableLiveData()
    val response get() = _response

    private val _productOrderList : MutableLiveData<List<Product>> by lazy { MutableLiveData(listOf()) }
    val productOrderList get() = _productOrderList

    private val fromDate : MutableStateFlow<String> by lazy { MutableStateFlow("") }
    private var toDate : String = ""
    private var statusQuery : String = ""

    //Pet order
    private val fromDatePet : MutableStateFlow<String> by lazy { MutableStateFlow("") }
    private var toDatePet : String = ""
    private var statusQueryPet : String = ""

    //Create Order
    val selectedProduct : MutableLiveData<Product?> = MutableLiveData(null)
    var previousSelectedProduct : Product? = null

    //Create Pet Order
    val selectedPet : MutableLiveData<Pet?> = MutableLiveData(null)
    val previousSelectedPet : MutableLiveData<Pet?> = MutableLiveData(null)


    //danh sách dữ liệu
    val orderItemList = fromDate.flatMapLatest {query ->
        repository.getOrderSource(query,toDate,statusQuery)
            .cachedIn(viewModelScope)
    }

    //thực hiện tìm kiếm
    fun filterOrder(from : String,to : String,status : String = ""){
        toDate = to
        statusQuery = status
        fromDate.value = from
    }


    fun getOrderDetail(id : String){
        try {
            viewModelScope.launch {
                val res = repository.getOrderDetail(id)
                if(res.result){
                    productOrderList.value = res.body
                }else{
                    Log.e(TAG,res.error)
                }


            }
        }catch (ex : Exception){
            Log.e(TAG,ex.toString())
        }
    }

    fun emptyProductOrderList(){
        productOrderList.value = listOf()
    }

    fun addSelectedProductToOrder(quantity : Int){
        val orderList = productOrderList.value!!.toMutableList()
        var isAdded = false

        val product = selectedProduct.value!!

        val iterate = orderList.listIterator()
        while (iterate.hasNext()){
            val p = iterate.next()
            if(p.id == product.id){
                p.quantity = quantity
                iterate.set(p)
                isAdded = true
                break
            }
        }

        if(!isAdded){
            product.quantity = quantity
            orderList.add(product)
        }

        productOrderList.value = orderList
        selectedProduct.value = null
    }

    fun changingProductOrder(quantity : Int){
        val product = selectedProduct.value!!
        if(previousSelectedProduct!!.id != product.id){
            val orderList = productOrderList.value!!.toMutableList()

            for(p in orderList){
                if(p.id == previousSelectedProduct!!.id){
                    orderList.remove(p)
                    break;
                }
            }

            product.quantity = quantity
            orderList.add(product)

            productOrderList.value = orderList

        }else if(previousSelectedProduct!!.quantity != quantity){
            val orderList = productOrderList.value!!.toMutableList()

            val iterate = orderList.listIterator()
            while (iterate.hasNext()){
                val p = iterate.next()
                if(p.id == previousSelectedProduct!!.id){
                    p.quantity = quantity
                    iterate.set(p)
                    break
                }
            }
            productOrderList.value = orderList
        }
    }

    fun selectProduct(product: Product){
        selectedProduct.value = product
        previousSelectedProduct = product
    }
    fun emptySelectedProduct(){
        selectedProduct.value = null
        previousSelectedProduct = null
    }

    fun isProductOrderEmpty()
        = productOrderList.value!!.isEmpty()


    fun createOrder(order : OrderCart){

        order.cart = productOrderList.value!!

        try {
            viewModelScope.launch {
                _response.value = repository.createOrder(order)
            }
        }catch (ex : Exception){
            Log.e(TAG,ex.toString())
        }
    }

    fun updateOrder(order: Order){
        try {
            viewModelScope.launch {
                _response.value = repository.updateOrder(order)
            }
        }catch (ex : Exception){
            Log.e(TAG,ex.toString())
        }
    }

    fun deleteOrder(order: Order){
        try {
            viewModelScope.launch {
                _response.value = repository.deleteOrder(order)
            }
        }catch (ex : Exception){
            Log.e(TAG,ex.toString())
        }
    }

    //Pet order
    val petOrderList = fromDatePet.flatMapLatest {query ->
        repository.getPetOrderSource(query,toDate,statusQuery)
            .cachedIn(viewModelScope)
    }

    //thực hiện tìm kiếm
    fun filterPetOrder(from : String,to : String,status : String = ""){
        toDatePet = to
        statusQueryPet = status
        fromDatePet.value = from
    }

    fun getPetDetail(id : String){
        try {
            viewModelScope.launch {
                val res = repository.getPetOrderDetail(id)
                if(res.result){
                    previousSelectedPet.value = res.body
                }else{
                    Log.e(TAG,res.error)
                }
            }
        }catch (ex : Exception){
            Log.e(TAG,ex.toString())
        }
    }


    fun createPetOrder(order : Order){

        val pet = previousSelectedPet.value!!
        order.petId = pet.id

        try {
            viewModelScope.launch {
                _response.value = repository.createPetOrder(order)
            }
        }catch (ex : Exception){
            Log.e(TAG,ex.toString())
        }
    }

    fun updatePetOrder(order: Order){
        try {
            viewModelScope.launch {
                _response.value = repository.updatePetOrder(order)
            }
        }catch (ex : Exception){
            Log.e(TAG,ex.toString())
        }
    }

    fun deletePetOrder(order: Order){
        try {
            viewModelScope.launch {
                _response.value = repository.deletePetOrder(order)
            }
        }catch (ex : Exception){
            Log.e(TAG,ex.toString())
        }
    }



}