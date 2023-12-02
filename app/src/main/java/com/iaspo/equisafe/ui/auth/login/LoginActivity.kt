package com.iaspo.equisafe.ui.auth.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.iaspo.equisafe.MainActivity
import com.iaspo.equisafe.R
import com.iaspo.equisafe.core.data.network.request.LoginRequest
import com.iaspo.equisafe.core.data.network.retrofit.ApiResult
import com.iaspo.equisafe.core.utils.gone
import com.iaspo.equisafe.core.utils.hideKeyboard
import com.iaspo.equisafe.core.utils.highlightWord
import com.iaspo.equisafe.core.utils.invisible
import com.iaspo.equisafe.core.utils.visible
import com.iaspo.equisafe.databinding.ActivityLoginBinding
import com.iaspo.equisafe.ui.auth.register.RegisterActivity
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable.combineLatest
import io.reactivex.disposables.CompositeDisposable
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val loginViewModel: LoginViewModel by viewModel()
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        setupAction()
        setupView()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    private fun setupAction() {

        binding.tvSignup.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            val loginRequest = LoginRequest(
                username = binding.inputLoginUsername.text.toString().trim(),
                password = binding.inputLoginPassword.text.toString().trim()
            )

            loginViewModel.loginUser(loginRequest).observe(this){
                it?.let { response ->
                    when(response) {
                        is ApiResult.Loading -> {
                            this.hideKeyboard()
                            binding.btnLogin.invisible()
                            binding.lottieLoading.visible()
                        }

                        is ApiResult.Success -> {
                            binding.btnLogin.visible()
                            binding.lottieLoading.gone()
                            Toast.makeText(this, response.data.message, Toast.LENGTH_LONG).show()
                            toDashboard()
                        }

                        is ApiResult.Error -> {
                            binding.btnLogin.visible()
                            binding.lottieLoading.gone()
                            Toast.makeText(this, response.errorMessage, Toast.LENGTH_LONG).show()
                        }

                        is ApiResult.Empty -> {
                            binding.btnLogin.visible()
                            binding.lottieLoading.gone()
                        }
                    }
                }
            }
        }
    }

    private fun toDashboard() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }


    private fun initView() {
        val usernameStream = RxTextView.textChanges(binding.inputLoginUsername)
            .skipInitialValue()
            .map {
                it.isEmpty()
            }

        val usernameSubscription = usernameStream.subscribe {
            showUsernameAlert(it)
        }

        val passwordStream = RxTextView.textChanges(binding.inputLoginPassword)
            .skipInitialValue()
            .map { password ->
                password.length < 8
            }

        val passwordSubscription = passwordStream.subscribe {
            showPasswordAlert(it)
        }

        val invalidFieldStream = combineLatest(
            usernameStream,
            passwordStream
        ){
            usernameInvalid: Boolean, passwordInvalid: Boolean ->
            !usernameInvalid && !passwordInvalid
        }

        val invalidFieldsStreamSubscription = invalidFieldStream.subscribe { isValid ->
            binding.btnLogin.isEnabled = isValid
        }

        compositeDisposable.addAll(usernameSubscription, passwordSubscription, invalidFieldsStreamSubscription)
    }

    private fun showPasswordAlert(isNotValid: Boolean) {
        binding.edLoginPassword.error = if (isNotValid) "Password > 8 characters" else null
    }

    private fun showUsernameAlert(isNotValid: Boolean) {
        binding.edLoginUsername.error = if (isNotValid) "Field cannot be empty" else null
    }


    private fun setupView() {
        binding.tvSignup.highlightWord(word = "Sign up", colorResId = R.color.green_main)
    }
}