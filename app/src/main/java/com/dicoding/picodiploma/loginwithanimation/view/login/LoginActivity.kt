package com.dicoding.picodiploma.loginwithanimation.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.dicoding.picodiploma.loginwithanimation.data.domain.Result
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityLoginBinding
import com.dicoding.picodiploma.loginwithanimation.data.local.UserPreference
import com.dicoding.picodiploma.loginwithanimation.viewmodel.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.main.MainActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupAction()
        playAnimation()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupViewModel() {
        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(this)
        )[LoginViewModel::class.java]
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val password = binding.passwordEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            loginViewModel.isLoading.observe(this) {
                showLoading(it)
            }
            loginViewModel.login(email, password).observe(this) { result ->
                when (result) {
                    is Result.Error -> {
                        showLoading(false)
                        Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                    }
                    is Result.Loading -> { showLoading(true) }
                    is Result.Success -> {
                        showLoading(false)
                        Toast.makeText(this, result.data.message, Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }
                }
            }
//            AlertDialog.Builder(this).apply {
//                setTitle("Yeah!")
//                setMessage("Anda berhasil login. Sudah tidak sabar ya?")
//                setPositiveButton("Lanjut") { _, _ ->
//                    val intent = Intent(context, MainActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//                    startActivity(intent)
//                    finish()
//                }
//                create()
//                show()
//            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(500)
        val message = ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(500)
        val emailTextView = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(500)
        val emailEditTextLayout = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val passwordTextView = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(500)
        val passwordEditTextLayout = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(title, message, emailTextView, emailEditTextLayout, passwordTextView, passwordEditTextLayout, login)
            startDelay = 500
        }.start()
    }

}