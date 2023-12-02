package com.iaspo.equisafe.ui.profile.changepassword

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.iaspo.equisafe.R
import com.iaspo.equisafe.core.data.network.request.ChangePasswordRequest
import com.iaspo.equisafe.core.data.network.retrofit.ApiResult
import com.iaspo.equisafe.core.utils.hideKeyboard
import com.iaspo.equisafe.databinding.ActivityChangePasswordBinding
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable.combineLatest
import io.reactivex.disposables.CompositeDisposable
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangePasswordBinding

    private val changePasswordViewModel: ChangePasswordViewModel by viewModel()

    private lateinit var compositeDisposable: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val window: Window? = this.window
        window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window?.statusBarColor = ContextCompat.getColor(this, R.color.green_main)

        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        compositeDisposable = CompositeDisposable()
        initView()
        setupAction()
    }

    private fun setupAction() {
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnSaveChangePassword.setOnClickListener {
            this.hideKeyboard()
            val oldPassword = binding.inputOldPassword.text.toString().trim()
            val newPassword = binding.inputChangeNewPassword.text.toString().trim()

            changePasswordViewModel.changePassword(
                changePasswordRequest = ChangePasswordRequest(
                    password = oldPassword,
                    newPassword = newPassword
                )
            ).observe(this){
                it?.let { response ->
                    when(response) {
                        is ApiResult.Loading -> {
                            binding.lottieLoading.visibility = View.VISIBLE
                            binding.btnSaveChangePassword.visibility = View.INVISIBLE
                        }

                        is ApiResult.Success -> {
                            binding.lottieLoading.visibility = View.GONE
                            binding.btnSaveChangePassword.visibility = View.VISIBLE
                            Toast.makeText(this, response.data.message, Toast.LENGTH_SHORT).show()
                        }

                        is ApiResult.Error -> {
                            binding.lottieLoading.visibility = View.GONE
                            binding.btnSaveChangePassword.visibility = View.VISIBLE
                            Toast.makeText(this, response.errorMessage, Toast.LENGTH_SHORT).show()
                        }

                        else ->{
                            binding.lottieLoading.visibility = View.GONE
                            binding.btnSaveChangePassword.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    private fun initView() {
        val oldPasswordStream = RxTextView.textChanges(binding.inputOldPassword)
            .skipInitialValue()
            .map { password ->
                password.length < 8
            }

        val oldPasswordSubscription = oldPasswordStream.subscribe {
            showOldPasswordAlert(it)
        }

        val newPasswordStream = RxTextView.textChanges(binding.inputChangeNewPassword)
            .skipInitialValue()
            .map { password ->
                password.length < 8
            }

        val newPasswordSubscription = newPasswordStream.subscribe {
            showNewPasswordAlert(it)
        }

        val passwordConfirmationStream =
            RxTextView.textChanges(binding.inputConfirmPassword)
                .skipInitialValue()
                .map { passwordConfirmation->
                    passwordConfirmation.toString() != binding.inputChangeNewPassword.text.toString()
                }

        val passwordConfirmationSubscription = passwordConfirmationStream.subscribe {
            showPasswordConfirmationAlert(it)
        }

        val invalidFieldStream = combineLatest(
            oldPasswordStream,
            newPasswordStream,
            passwordConfirmationStream
        ){
            oldPasswordInvalid: Boolean, newPasswordInvalid: Boolean, passwordConfirmationInvalid: Boolean ->
            !oldPasswordInvalid && !newPasswordInvalid && !passwordConfirmationInvalid
        }

        val invalidFieldStreamSubscription = invalidFieldStream.subscribe {
            binding.btnSaveChangePassword.isEnabled = it
        }

        compositeDisposable.addAll(oldPasswordSubscription, newPasswordSubscription, passwordConfirmationSubscription, invalidFieldStreamSubscription)
    }

    private fun showNewPasswordAlert(isNotValid: Boolean) {
        binding.edChangeNewPassword.isEndIconVisible = !isNotValid
        binding.inputChangeNewPassword.error = if (isNotValid) "Password > 8 characters" else null
    }

    private fun showPasswordConfirmationAlert(isNotValid: Boolean) {
        binding.edConfirmPassword.isEndIconVisible = !isNotValid
        binding.inputConfirmPassword.error = if (isNotValid) "Password must be same" else null
    }

    private fun showOldPasswordAlert(isNotValid: Boolean) {
        binding.edOldPassword.isEndIconVisible = !isNotValid
        binding.inputOldPassword.error = if (isNotValid) "Password > 8 characters" else null
    }
}