package com.healthit.application.model.response.loginRegister

import com.healthit.application.model.response.ApiError

data class LoginResponse(val data: Login?, val errors:List<ApiError>?)

data class Login(val customerAccessTokenCreate: CustomerAccessTokenCreate?)

data class CustomerAccessTokenCreate(val userErrors: List<Error>?, val customerAccessToken: CustomerAccessToken?)

data class Error(val field: String?, val message: String?)

data class CustomerAccessToken(val accessToken: String?, val expiresAt: String?)