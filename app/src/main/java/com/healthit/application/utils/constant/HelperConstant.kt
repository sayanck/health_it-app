package com.healthit.application.utils.constant

object HelperConstant {

    //Change Language
    const val ENGLISH = "en"
    const val SPANISH = "es"

    enum class UserType {
        PATIENT, DOCTOR
    }

    object CollectionName {
        const val sUsers = "users"
        const val sAppointments = "appointments"
    }

    object ParamsKey {
        const val sDOCTOR_SPECIALITY = "doctorSpeciality"
        const val sPATIENT_ID = "patientId"
        const val sDOCTOR_ID = "doctorId"
        const val sACCESS_TOKEN = "accessToken"
    }

    object IntentParams {
        const val sDOCTOR_CATEGORY = "doctorCategory"
        const val sDOCTOR_DETAIL = "doctorDetail"
        const val sAPPOINTMENTS_DETAIL = "appointmentsDetail"
        const val sNOTIFICATION_DATA = "NotificationData"
    }

    enum class Time {
        MORNING, EVENING
    }

    enum class AppointmentStatus {
        PENDING, CONFIRMED, DECLINED, INPROGRESS, COMPLETED
    }

    enum class NotificationType {
        APPOINTMENT, CHAT, VIDEOCALL,
    }

}