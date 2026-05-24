package com.example.t5_mobile.data.model

data class PatientResponse(
    val success: Boolean,
    val message: String,
    val data: List<Patient>
)