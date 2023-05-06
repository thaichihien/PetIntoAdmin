package com.mobye.petintoadmin.models.apimodels

class ApiResponse<T>(
    var result : Boolean,
    var reason : String,
    var error : String,
    var body : T? = null
) {
    companion object{
        fun convertToAny(response: ApiResponse<*>)
                = ApiResponse<Any>(
            result = response.result,
            reason = response.reason,
            error = response.error
        )
    }
}