package com.mobye.petintoadmin.network

import com.mobye.petintoadmin.models.*
import com.mobye.petintoadmin.models.apimodels.ApiResponse
import com.mobye.petintoadmin.models.apimodels.OrderCart
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface AdminApi {

    // Product
    @GET("/admin/product/get")
    suspend fun getProduct(
        @Query("page") page : Int,
        @Query("query") query : String = ""
    ) : ApiResponse<List<Product>>

    @POST("/admin/product/create")
    suspend fun createProduct(
        @Body product : Product
    ) : ApiResponse<String>

    @POST("/admin/product/update")
    suspend fun updateProduct(
        @Body product : Product
    ) : ApiResponse<Any>

    @POST("/admin/product/delete")
    suspend fun deleteProduct(
        @Body product : Product
    ) : ApiResponse<Any>

    //Pet
    @GET("/admin/pet/get")
    suspend fun getPet(
        @Query("page") page : Int,
        @Query("query") query : String = ""
    ) : ApiResponse<List<Pet>>

    @POST("/admin/pet/create")
    suspend fun createPet(
        @Body pet : Pet
    ) : ApiResponse<String>

    @POST("/admin/pet/update")
    suspend fun updatePet(
        @Body pet : Pet
    ) : ApiResponse<Any>

    @POST("/admin/pet/delete")
    suspend fun deletePet(
        @Body pet : Pet
    ) : ApiResponse<Any>

    //Booking
    @GET("/admin/booking/get")
    suspend fun getBooking(
        @Query("page") page : Int,
        @Query("from") from : String = "",
        @Query("to") to : String = "",
        @Query("status") status : String = ""
    ) : ApiResponse<List<Booking>>

    @POST("/admin/booking/create")
    suspend fun createBooking(
        @Body booking: Booking
    ) : ApiResponse<Booking>

    @POST("/admin/booking/update")
    suspend fun updateBooking(
        @Body booking: Booking
    ) : ApiResponse<Any>

    @POST("/admin/booking/delete")
    suspend fun deleteBooking(
        @Body booking: Booking
    ) : ApiResponse<Any>

    @GET("/admin/booking/detail")
    suspend fun getBookingDetail(
        @Query("id") id : String
    ) : Response<ApiResponse<Booking>>


    //Order
    @GET("/admin/order/get")
    suspend fun getOrder(
        @Query("page") page : Int,
        @Query("from",encoded = false) from : String = "",
        @Query("to", encoded = false) to : String = "",
        @Query("status") status : String = ""
    ) : ApiResponse<List<Order>>

    @GET("/admin/order/detail")
    suspend fun getOrderDetail(
        @Query("id") id : String = ""
    ) : ApiResponse<List<Product>>

    @POST("/admin/order/create")
    suspend fun createOrder(
        @Body order: OrderCart
    ) : ApiResponse<String>

    @POST("/admin/order/update")
    suspend fun updateOrder(
        @Body order: Order,
        @Query("notify") notify : Boolean = false
    ) : ApiResponse<Any>

    @POST("/admin/order/delete")
    suspend fun deleteOrder(
        @Body order: Order
    ) : ApiResponse<Any>

    //Pet order
    @GET("/admin/pet-order/get")
    suspend fun getPetOrder(
        @Query("page") page : Int,
        @Query("from") from : String = "",
        @Query("to") to : String = "",
        @Query("status") status : String = ""
    ) : ApiResponse<List<Order>>

    @GET("/admin/pet-order/detail")
    suspend fun getPetOrderDetail(
        @Query("id") id : String = ""
    ) : ApiResponse<Pet>

    @POST("/admin/pet-order/create")
    suspend fun createPetOrder(
        @Body order: Order
    ) : ApiResponse<String>

    @POST("/admin/pet-order/update")
    suspend fun updatePetOrder(
        @Body order: Order,
        @Query("notify") notify : Boolean = false
    ) : ApiResponse<Any>

    @POST("/admin/pet-order/delete")
    suspend fun deletePetOrder(
        @Body order: Order
    ) : ApiResponse<Any>


    //Profile

    @POST("/admin/account/get")
    suspend fun getAdmin(
        @Query("id") id : String,
        @Body token : Map<String, String>
    ) : Response<ApiResponse<Admin>>

    @GET("/admin/account/check")
    suspend fun checkAdmin(
        @Query("id") id : String,
    ) : Response<ApiResponse<Any>>

    @GET("/admin/report/get")
    suspend fun getReport(
        @Query("page") page : Int
    ) : ApiResponse<List<Report>>

    @GET("/admin/report/delete")
    suspend fun deleteReport(
        @Query("id") id : String
    ) : ApiResponse<Any>
}