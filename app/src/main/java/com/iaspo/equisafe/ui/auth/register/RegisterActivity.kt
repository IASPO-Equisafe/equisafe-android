package com.iaspo.equisafe.ui.auth.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.iaspo.equisafe.R
import com.iaspo.equisafe.core.data.network.request.RegisterRequest
import com.iaspo.equisafe.core.data.network.retrofit.ApiResult
import com.iaspo.equisafe.core.utils.gone
import com.iaspo.equisafe.core.utils.hideKeyboard
import com.iaspo.equisafe.core.utils.highlightWord
import com.iaspo.equisafe.core.utils.invisible
import com.iaspo.equisafe.core.utils.visible
import com.iaspo.equisafe.databinding.ActivityRegisterBinding
import com.iaspo.equisafe.ui.auth.login.LoginActivity
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {

    private val registerViewModel: RegisterViewModel by viewModel()

    private lateinit var binding: ActivityRegisterBinding

    private var emergencyNameValue: String? = null
    private var emergencyNumberValue: String? = null

    private var isShowed: Boolean = false
    private var isEmergencyFilled: Boolean = false
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        setEmergency()
        setupAction()
    }

    private fun setupAction() {
        binding.btnRegister.setOnClickListener {
            val registerRequest = RegisterRequest(
                username = binding.inputRegisterUsername.text.toString().trim(),
                password = binding.inputRegisterPassword.text.toString().trim(),
                name = binding.inputRegisterFullname.text.toString().trim(),
                emergencyName = binding.layoutSetEmergency.inputRegisterContactName.text.toString().trim(),
                emergencyTelp = binding.layoutSetEmergency.inputRegisterContactNumber.text.toString().trim()
            )

            this.registerViewModel.registerUser(registerRequest).observe(this@RegisterActivity){
                it.let { response ->
                    when(response) {
                        is ApiResult.Loading -> {
                            this.hideKeyboard()
                            binding.btnRegister.invisible()
                            binding.lottieLoading.visible()
                        }

                        is ApiResult.Success -> {
                            binding.lottieLoading.gone()
                            Toast.makeText(this, response.data.message, Toast.LENGTH_LONG).show()
                            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                        }

                        is ApiResult.Error -> {
                            binding.btnRegister.visible()
                            binding.lottieLoading.gone()
                            Toast.makeText(this, response.errorMessage, Toast.LENGTH_LONG).show()
                        }

                        is ApiResult.Empty -> {
                            binding.btnRegister.visible()
                            binding.lottieLoading.gone()
                            Toast.makeText(this, "Tafta Ganteng", Toast.LENGTH_LONG).show()
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

    private fun setEmergency() {
        binding.btnAddEmergency.setOnClickListener {
            doShow(emergencyNameValue, emergencyNumberValue)
        }

        binding.tvSignup.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }

        binding.layoutSetEmergency.bgChangeAddress.setOnClickListener {
            doHide()
        }

        binding.layoutSetEmergency.btnClose.setOnClickListener {
            doHide()
        }

        binding.viewBtnChange.setOnClickListener {
            doShow(emergencyNameValue, emergencyNumberValue)
        }

        binding.layoutSetEmergency.btnSetEmergency.setOnClickListener {
            emergencyNameValue = binding.layoutSetEmergency.inputRegisterContactName.text.toString().trim()
            emergencyNumberValue = "+62" + binding.layoutSetEmergency.inputRegisterContactNumber.text.toString().trim()

            dataPass(emergencyNameValue.orEmpty(), emergencyNumberValue.orEmpty())
        }
    }

    private fun dataPass(emergencyNameValue: String, emergencyNumberValue: String) {
        binding.emergencyLayout.visibility = View.VISIBLE
        binding.emergencyNameValue.text = emergencyNameValue
        binding.emergencyNumberValue.text = emergencyNumberValue
        binding.btnAddEmergency.visibility = View.GONE
        isEmergencyFilled = true
        doHide()
    }

    private fun doHide() {
        val bgEmergency = binding.layoutSetEmergency.bgChangeAddress
        val containerEmergency = binding.layoutSetEmergency.containerSetEmergency
        bgEmergency.visibility = View.VISIBLE
        containerEmergency.visibility = View.VISIBLE
        bgEmergency.animate()
            .alpha(0f)
            .setDuration(200L)
            .start()
        containerEmergency.animate()
            .alpha(0f)
            .translationY(50F)
            .setDuration(200L)
            .start()
        lifecycleScope.launch(Dispatchers.IO){
            delay(200L)
            withContext(Dispatchers.Main) {
                doGone()
            }
        }
        this.isShowed = false
    }

    private fun doGone() {
        val bgEmergency = binding.layoutSetEmergency.bgChangeAddress
        val containerEmergency = binding.layoutSetEmergency.containerSetEmergency

        bgEmergency.visibility = View.GONE
        containerEmergency.visibility = View.GONE
    }

    private fun doShow(emergencyNameValue: String?, emergencyNumberValue: String?) {
        val bgEmergency = binding.layoutSetEmergency.bgChangeAddress
        val containerEmergency = binding.layoutSetEmergency.containerSetEmergency

        bgEmergency.visibility = View.VISIBLE
        containerEmergency.visibility = View.VISIBLE

        bgEmergency.animate()
            .alpha(0f)
            .setDuration(0)
            .start()
        containerEmergency.animate()
            .alpha(0f)
            .translationY(50f)
            .setDuration(0)
            .start()
        bgEmergency.animate()
            .alpha(1f)
            .setDuration(200L)
            .setStartDelay(50)
            .start()
        containerEmergency.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(200L)
            .setStartDelay(50)
            .start()

        if (!emergencyNameValue.isNullOrEmpty() && !emergencyNumberValue.isNullOrEmpty()) {
            binding.layoutSetEmergency.inputRegisterContactName.setText(emergencyNameValue)
            binding.layoutSetEmergency.inputRegisterContactNumber.setText(emergencyNumberValue)
        }

        this.isShowed = true
    }

    private fun initView() {
        binding.tvSignup.highlightWord("Sign in", R.color.green_main)

        val fullNameStream = RxTextView.textChanges(binding.inputRegisterFullname)
            .skipInitialValue()
            .map { fullName ->
                fullName.isEmpty()
            }

        val fullNameSubscription = fullNameStream.subscribe{
            showFullNameAlert(it)
        }

            val usernameStream = RxTextView.textChanges(binding.inputRegisterUsername)
                .skipInitialValue()
                .map { username ->
                    username.isEmpty()
                }

            val usernameSubscription = usernameStream.subscribe {
                showUsernameAlert(it)
            }

        val passwordStream = RxTextView.textChanges(binding.inputRegisterPassword)
            .skipInitialValue()
            .map { password ->
                password.length < 8
            }

        val passwordSubscription = passwordStream.subscribe {
            showPasswordAlert(it)
        }

        val passwordConfirmationStream =
            RxTextView.textChanges(binding.inputRegisterConfirmPassword)
                .skipInitialValue()
                .map { passwordConfirmation->
                    passwordConfirmation.toString() != binding.inputRegisterPassword.text.toString()
                }

        val passwordConfirmationSubscription = passwordConfirmationStream.subscribe {
            showPasswordConfirmationAlert(it)
        }

        val emergencyNameStream = RxTextView.textChanges(binding.layoutSetEmergency.inputRegisterContactName)
            .skipInitialValue()
            .map {
                it.isEmpty()
            }

        val emergencyNameSubscription = emergencyNameStream.subscribe {
            showEmergencyNameAlert(it)
        }

        val emergencyNumberStream = RxTextView.textChanges(binding.layoutSetEmergency.inputRegisterContactNumber)
            .skipInitialValue()
            .map {
                it.isEmpty()
            }

        val emergencyNumberSubscription = emergencyNumberStream.subscribe {
            showEmergencyNumberAlert(it)
        }

        val invalidFieldsEmergencyStream = Observable.combineLatest(
            emergencyNameStream,
            emergencyNumberStream,
        ) { emergencyNameInvalid: Boolean, emergencyNumberInvalid: Boolean ->
            !emergencyNameInvalid && !emergencyNumberInvalid
        }

        val invalidFieldsEmergencyStreamSubscription = invalidFieldsEmergencyStream.subscribe { isValid ->
            binding.layoutSetEmergency.btnSetEmergency.isEnabled = isValid
        }

        val invalidFieldsStream = Observable.combineLatest(
            fullNameStream,
            usernameStream,
            passwordStream,
            passwordConfirmationStream,
            emergencyNameStream,
            emergencyNumberStream
        ) { fullnameInvalid: Boolean, usernameInvalid: Boolean, passswordInvalid: Boolean, passwordConfirmationInvalid: Boolean, emergencyNameInvalid: Boolean, emergencyNumberInvalid: Boolean ->
            !fullnameInvalid && !usernameInvalid && !passswordInvalid && !passwordConfirmationInvalid && !emergencyNameInvalid && !emergencyNumberInvalid
        }

        val invalidFieldsStreamSubscription = invalidFieldsStream.subscribe { isValid ->
            binding.btnRegister.isEnabled = isValid
        }

        compositeDisposable.addAll(usernameSubscription, fullNameSubscription, passwordSubscription, passwordConfirmationSubscription, invalidFieldsStreamSubscription, emergencyNameSubscription, emergencyNumberSubscription, invalidFieldsEmergencyStreamSubscription)
    }

    private fun showPasswordConfirmationAlert(isNotValid: Boolean) {
        binding.edRegisterConfirmPassword.isEndIconVisible = !isNotValid
        binding.inputRegisterConfirmPassword.error = if (isNotValid) "Password must be same" else null
    }

    private fun showPasswordAlert(isNotValid: Boolean) {
        binding.edRegisterPassword.isEndIconVisible = !isNotValid
        binding.inputRegisterPassword.error = if (isNotValid) "Password > 8 characters" else null
    }

    private fun showUsernameAlert(isNotValid: Boolean) {
        binding.inputRegisterUsername.error = if (isNotValid) "Field cannot be empty" else null
    }

    private fun showFullNameAlert(isNotValid: Boolean) {
        binding.inputRegisterFullname.error = if (isNotValid) "Field cannot be empty" else null
    }

    private fun showEmergencyNameAlert(isNotValid: Boolean) {
        binding.layoutSetEmergency.inputRegisterContactName.error = if (isNotValid) "Field cannot be empty" else null
    }

    private fun showEmergencyNumberAlert(isNotValid: Boolean) {
        binding.layoutSetEmergency.inputRegisterContactNumber.error = if (isNotValid) "Field cannot be empty" else null
    }
}