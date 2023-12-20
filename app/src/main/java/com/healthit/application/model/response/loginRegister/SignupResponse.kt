package com.healthit.application.model.response.loginRegister

import com.healthit.application.model.response.ApiError

data class SignupResponse(val data: Data, val errors: List<ApiError>?)

data class Data(val customerCreate: CustomerCreate?)

data class CustomerCreate(val customer: Customer?, val customerUserErrors: List<CustomerUserError>)

data class Customer(val id: String)

data class CustomerUserError(val code: String, val field: List<String>, val message: String)