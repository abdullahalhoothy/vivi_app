package com.app.honey.data.remote

object ApiNames {
//    const val BASE_URL = "https://meftii.com:8443/eprescription/"
    const val BASE_URL = "https://testing.com:8443/online/"
    const val LOGIN = "${BASE_URL}app/login"




    //Patient
    const val PATIENT_LIST = "${BASE_URL}patient/patientlist"

}

object HeadersNames {
    const val EMAIL = "email"
    const val LANGUAGE = "language"
    const val CHANNEL = "channel"
}

object HeaderValues {
    const val LANGUAGE = "english"
    const val CHANNEL = "android"
}