package com.healthit.application.model.response.loginRegister

import com.healthit.application.model.response.ApiError


data class CustomerDetailsResponse(val data: CustomerData, val errors: List<ApiError>?)
data class CustomerData(val customer: CustomerDetails?)
data class CustomerDetails(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val phone: String = "",
    val acceptsMarketing: Boolean = false,
    val addresses: Address?,
    val defaultAddress: AddressNode?
) {
    fun getPhoneWithoutPhoneCode(): String {
        return phone.replace("+52", "")
    }
}

data class Address(val edges: List<AddressEdges>?)
data class AddressEdges(val node: AddressNode?)

data class AddressNode(
    val id: String,
    var address1: String?,
    var address2: String?,
    var zip: String,
    var city: String,
    var province: String,
    var country: String,
    val firstName: String,
    val lastName: String,
    var isDefault: Boolean = false,
    var isSelected: Boolean = false,
    var isZipSelected: Boolean = false,
    var fullAddress: String = ""
)