package com.healthit.application.model.response

data class ApiFieldError(val code: String, val field: List<String>, val message: String)