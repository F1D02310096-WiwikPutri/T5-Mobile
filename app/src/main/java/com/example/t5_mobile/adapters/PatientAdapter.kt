package com.example.t5_mobile.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.t5_mobile.databinding.ItemPatientBinding
import com.example.t5_mobile.data.model.Patient

class PatientAdapter(
    private var patientList: List<Patient>
) : RecyclerView.Adapter<PatientAdapter.PatientViewHolder>() {

    inner class PatientViewHolder(private val binding: ItemPatientBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(patient: Patient) {
            binding.apply {
                tvNama.text = patient.nama

                val gender = if (patient.jenisKelamin == "L") "Laki-laki" else "Perempuan"
                tvGenderTgl.text = "$gender • ${patient.tanggalLahir}"
                tvAlamat.text = patient.alamat
                tvTelepon.text = patient.noTelepon
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientViewHolder {
        val binding = ItemPatientBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PatientViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PatientViewHolder, position: Int) {
        holder.bind(patientList[position])
    }

    override fun getItemCount(): Int = patientList.size

    fun updateData(newList: List<Patient>) {
        patientList = newList
        notifyDataSetChanged()
    }
}