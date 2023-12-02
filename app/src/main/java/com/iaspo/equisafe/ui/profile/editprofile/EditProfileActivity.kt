package com.iaspo.equisafe.ui.profile.editprofile

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.iaspo.equisafe.R
import com.iaspo.equisafe.core.data.network.request.SaveDataRequest
import com.iaspo.equisafe.core.data.network.retrofit.ApiResult
import com.iaspo.equisafe.core.domain.model.UserModel
import com.iaspo.equisafe.core.utils.hideKeyboard
import com.iaspo.equisafe.databinding.ActivityEditProfileBinding
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable.combineLatest
import io.reactivex.disposables.CompositeDisposable
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding

    private val editProfileViewModel: EditProfileViewModel by viewModel()

     private var fullname: String? = null
     private var username: String? = null

    private lateinit var compositeDisposable: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val window: Window? = this.window
        window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window?.statusBarColor = ContextCompat.getColor(this, R.color.green_main)

        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        setupAction()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    private fun setupAction() {
        val usernameStream = RxTextView.textChanges(binding.edEditUsername)
            .map { username ->
                username.isEmpty()
            }

        val usernameSubscription = usernameStream.subscribe {
            showUsernameAlert(it)
        }

        val fullNameStream = RxTextView.textChanges(binding.edEditName)
            .map { fullName ->
                fullName.isEmpty()
            }

        val fullNameSubscription = fullNameStream.subscribe {
            showFullnameAlert(it)
        }

        val invalidFieldStream = combineLatest(
            usernameStream,
            fullNameStream
        ){
            usernameInvalid, fullNameInvalid ->
            !usernameInvalid && !fullNameInvalid
        }

        val invalidFieldSubscription = invalidFieldStream.subscribe {
            binding.btnSaveEditProfile.isEnabled = it
        }

        compositeDisposable.addAll(fullNameSubscription, usernameSubscription, invalidFieldSubscription)

        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnSaveEditProfile.setOnClickListener {
            this.hideKeyboard()
            val usernameField = binding.edEditUsername.text.toString().trim()
            val fullNameField = binding.edEditName.text.toString().trim()

            if (usernameField == username && fullNameField == fullname) {
                finish()
            } else {
                editProfileViewModel.saveAccountData(
                    saveDataRequest = SaveDataRequest(
                        username = usernameField,
                        name = fullNameField
                    )
                ).observe(this){
                    it?.let { response ->
                        when(response) {
                            is ApiResult.Loading -> {
                                binding.lottieLoading.visibility = View.VISIBLE
                                binding.btnSaveEditProfile.visibility = View.INVISIBLE
                            }

                            is ApiResult.Success -> {
                                binding.lottieLoading.visibility = View.GONE
                                binding.btnSaveEditProfile.visibility = View.VISIBLE
                                Toast.makeText(this, response.data.message, Toast.LENGTH_LONG).show()
                            }

                            is ApiResult.Error -> {
                                binding.lottieLoading.visibility = View.GONE
                                binding.btnSaveEditProfile.visibility = View.VISIBLE
                                Toast.makeText(this, response.errorMessage, Toast.LENGTH_LONG).show()
                            }

                            else ->{
                                binding.lottieLoading.visibility = View.GONE
                                binding.btnSaveEditProfile.visibility = View.VISIBLE
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showFullnameAlert(isNotValid: Boolean) {
        binding.edEditName.error = if (isNotValid) "Field cannot be empty" else null
    }

    private fun showUsernameAlert(isNotValid: Boolean) {
        binding.edEditUsername.error = if (isNotValid) "Field cannot be empty" else null
    }

    private fun initView() {
        compositeDisposable = CompositeDisposable()

        val accountData = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra("accountData", UserModel::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("accountData")
        }

        fullname = accountData?.fullName
        username = accountData?.username

        binding.edEditName.setText(fullname)
        binding.edEditUsername.setText(username)
    }
}