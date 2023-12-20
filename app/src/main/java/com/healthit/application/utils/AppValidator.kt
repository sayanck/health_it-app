package com.healthit.application.utils

class AppValidator {

    companion object {
        fun isValidEmail(email: String): Boolean {
            val regex = "^[\\w-_.+]*[\\w-_.]@([\\w-]+\\.)+[\\w]+[\\w]$"
            return email.matches(regex.toRegex())
        }

        private var signUpMobile: String = "[0-9]{10}"
        private var mobile: String = "[0-9]+"

        fun isSignUpValidMobileNumber(phoneNumber: String): Boolean {
            return phoneNumber.matches(signUpMobile.toRegex())
        }

        fun isValidMobileNumber(phoneNumber: String): Boolean {
            return phoneNumber.matches(mobile.toRegex())
        }

        fun isValidName(name: String): Boolean {
            return name.matches("[a-zA-Z ]{2,}".toRegex())
        }

        fun isValidPassword(password: String): Boolean {
            return if (!password.contains("[A-Z]".toRegex())) {
                false
            } else if (!password.contains("[a-z]".toRegex())) {
                false
            } else if (!password.contains("[0-9]".toRegex())) {
                false
            } else password.contains("[!\"#\$%&'()*+,-./:;\\\\<=>?@\\[\\]^_`{|}~]".toRegex())
        }

        fun isValidLoginPassword(password: String): Boolean {
            return password != ""
        }

        fun isValidPasswordFormat(password: String): Boolean {
            return if (!password.contains("[A-Z]".toRegex())) {
                false
            } else if (!password.contains("[a-z]".toRegex())) {
                false
            } else if (!password.contains("[0-9]".toRegex())) {
                false
            } else password.contains("[!\"#$%&'()*+,-./:;\\\\<=>?@\\[\\]^_`{|}~]".toRegex())
        }

        fun isValidAddress(address: String): Boolean {
            return address.isNotEmpty()
        }

        fun isValidPinCode(pinCode: String): Boolean {
            return if (pinCode.contains(",") || pinCode.contains(".")) {
                false
            } else {
                pinCode.length in 5..7
            }
        }
    }
}