package com.example.t5_mobile.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.t5_mobile.databinding.ActivityLoginBinding
import com.example.t5_mobile.data.api.ApiClient
import com.example.t5_mobile.utils.SessionManager
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        if (sessionManager.isLoggedIn()) {
            startActivity(Intent(this, PatientListActivity::class.java))
            finish()
        }

        binding.btnLogin.setOnClickListener {
            performLogin()
        }
    }

    private fun performLogin() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        if (email.isEmpty()) {
            binding.etEmail.error = "Email tidak boleh kosong"
            return
        }

        if (password.isEmpty()) {
            binding.etPassword.error = "Password tidak boleh kosong"
            return
        }

        setLoading(true)

        lifecycleScope.launch {
            try {
                val request = mapOf("email" to email, "password" to password)
                val response = ApiClient.apiService.login(request)

                if (response.isSuccessful && response.body()?.success == true) {
                    val data = response.body()?.data
                    val token = data?.token ?: ""
                    val userName = data?.user?.name ?: ""

                    sessionManager.saveToken(token)
                    sessionManager.saveUserName(userName)

                    Toast.makeText(this@LoginActivity, "Login Berhasil", Toast.LENGTH_SHORT).show()

                    startActivity(Intent(this@LoginActivity, PatientListActivity::class.java))
                    finish()

                } else {
                    val message = response.body()?.message ?: "Login Gagal"
                    Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                Toast.makeText(this@LoginActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                e.printStackTrace()
            } finally {
                setLoading(false)
            }
        }
    }

    private fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.btnLogin.isEnabled = false
            binding.btnLogin.text = ""
        } else {
            binding.progressBar.visibility = View.GONE
            binding.btnLogin.isEnabled = true
            binding.btnLogin.text = "LOGIN"
        }
    }
}