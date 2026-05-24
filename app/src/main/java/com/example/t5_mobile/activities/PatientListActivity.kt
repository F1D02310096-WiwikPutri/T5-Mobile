package com.example.t5_mobile.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.t5_mobile.databinding.ActivityPatientListBinding
import com.example.t5_mobile.data.api.ApiClient
import com.example.t5_mobile.data.model.Patient
import com.example.t5_mobile.ui.adapter.PatientAdapter
import com.example.t5_mobile.utils.SessionManager
import kotlinx.coroutines.launch

class PatientListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPatientListBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var adapter: PatientAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatientListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        // Cek session
        if (!sessionManager.isLoggedIn()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        setupToolbar()
        setupRecyclerView()
        setupUserInfo()
        setupListeners()
        loadPatients()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Data Pasien"
    }

    private fun setupRecyclerView() {
        adapter = PatientAdapter(emptyList())
        binding.rvPatients.layoutManager = LinearLayoutManager(this)
        binding.rvPatients.adapter = adapter
    }

    private fun setupUserInfo() {
        val userName = sessionManager.getUserName() ?: "User"
        binding.tvUserName.text = "Selamat Datang, $userName"
    }

    private fun setupListeners() {
        binding.btnLogout.setOnClickListener {
            sessionManager.clearSession()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun loadPatients() {
        setLoading(true)

        lifecycleScope.launch {
            try {
                val token = sessionManager.getToken() ?: ""
                val authHeader = "Bearer $token"
                val response = ApiClient.apiService.getPatients(authHeader)

                if (response.isSuccessful && response.body()?.success == true) {
                    val patients = response.body()?.data ?: emptyList()
                    displayPatients(patients)
                } else {
                    val message = response.body()?.message ?: "Gagal mengambil data"
                    showError(message)

                    // Jika token expired
                    if (response.code() == 401) {
                        sessionManager.clearSession()
                        startActivity(Intent(this@PatientListActivity, LoginActivity::class.java))
                        finish()
                    }
                }

            } catch (e: Exception) {
                showError("Error: ${e.message}")
                e.printStackTrace()
            } finally {
                setLoading(false)
            }
        }
    }

    private fun displayPatients(patients: List<Patient>) {
        if (patients.isEmpty()) {
            binding.tvEmpty.visibility = View.VISIBLE
            binding.rvPatients.visibility = View.GONE
        } else {
            binding.tvEmpty.visibility = View.GONE
            binding.rvPatients.visibility = View.VISIBLE
            adapter.updateData(patients)
        }
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun setLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.rvPatients.visibility = if (isLoading) View.GONE else View.VISIBLE
    }
}