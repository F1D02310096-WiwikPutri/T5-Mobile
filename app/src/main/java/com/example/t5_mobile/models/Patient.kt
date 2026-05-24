package com.example.t5_mobile.data.model

import com.google.gson.annotations.SerializedName

data class Patient(
    @SerializedName("id") val id: Int,
    @SerializedName("nama") val nama: String,
    @SerializedName("jenisKelamin") val jenisKelamin: String,
    @SerializedName("tanggalLahir") val tanggalLahir: String,
    @SerializedName("alamat") val alamat: String,
    @SerializedName("noTelepon") val noTelepon: String
)