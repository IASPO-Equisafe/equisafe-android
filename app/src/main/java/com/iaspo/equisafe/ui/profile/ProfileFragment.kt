package com.iaspo.equisafe.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import com.iaspo.equisafe.R
import com.iaspo.equisafe.core.data.network.retrofit.ApiResult
import com.iaspo.equisafe.core.domain.model.UserModel
import com.iaspo.equisafe.core.utils.withNotNull
import com.iaspo.equisafe.databinding.FragmentProfileBinding
import com.iaspo.equisafe.ui.auth.splashscreen.SplashScreenActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val profileViewModel: ProfileViewModel by viewModel()
    private var userModel: UserModel? = null

    private var dataIsLoaded: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val window: Window? = activity?.window
        window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.green_main)

        if (!dataIsLoaded) {
            initView()
            dataIsLoaded = true
        }
        setupAction()
    }

    override fun onResume() {
        super.onResume()
        if (!dataIsLoaded) {
            initView()
            dataIsLoaded = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val window: Window? = activity?.window
        window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.white)
    }

    private fun setupAction() {
        binding.btnLogout.setOnClickListener {
            logout()
        }

        binding.btnEditProfile.setOnClickListener {
            userModel.withNotNull { userModel ->
                dataIsLoaded = false
                val toEditProfileActivity =
                    ProfileFragmentDirections.actionNavigationProfileToEditProfileActivity(userModel)
                it.findNavController().navigate(toEditProfileActivity)
            }
        }

        binding.btnChangePassword.setOnClickListener {
            dataIsLoaded = false
            val toChangePasswordActivity = ProfileFragmentDirections.actionNavigationProfileToChangePasswordActivity()
            it.findNavController().navigate(toChangePasswordActivity)
        }

        binding.btnAbout.setOnClickListener {
            val toAboutActivity = ProfileFragmentDirections.actionNavigationProfileToAboutActivity()
            it.findNavController().navigate(toAboutActivity)
        }

        binding.btnHelp.setOnClickListener {
            val toHelpActivity = ProfileFragmentDirections.actionNavigationProfileToHelpActivity()
            it.findNavController().navigate(toHelpActivity)
        }
    }

    private fun logout() {
        profileViewModel.logout.observe(viewLifecycleOwner) {
            it?.let { response ->
                when (response) {
                    is ApiResult.Loading -> {
                        binding.lottieLoading.visibility = View.VISIBLE
                        binding.btnLogout.visibility = View.INVISIBLE
                    }

                    is ApiResult.Success -> {
                        binding.lottieLoading.visibility = View.GONE
                        binding.btnLogout.visibility = View.VISIBLE
                        Toast.makeText(requireContext(), response.data.message, Toast.LENGTH_LONG)
                            .show()
                        moveActivity()
                    }

                    is ApiResult.Error -> {
                        binding.lottieLoading.visibility = View.GONE
                        binding.btnLogout.visibility = View.VISIBLE
                        Toast.makeText(requireContext(), response.errorMessage, Toast.LENGTH_LONG)
                            .show()
                    }

                    else -> {
                        binding.lottieLoading.visibility = View.GONE
                        binding.btnLogout.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun moveActivity() {
        val intent = Intent(requireContext(), SplashScreenActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun initView() {
        profileViewModel.getAccountData().observe(viewLifecycleOwner) {
            it?.let { response ->
                when (response) {
                    is ApiResult.Loading -> {
                        binding.lottieLoadingName.visibility = View.VISIBLE
                    }

                    is ApiResult.Success -> {
                        binding.lottieLoadingName.visibility = View.GONE
                        binding.tvNameProfile.text = response.data.data?.name
                        userModel = UserModel(
                            username = response.data.data?.username,
                            fullName = response.data.data?.name,
                            emergencyName = null,
                            emergencyNumber = null
                        )

                    }

                    is ApiResult.Error -> {
                        binding.lottieLoadingName.visibility = View.GONE
                        Toast.makeText(requireContext(), response.errorMessage, Toast.LENGTH_LONG)
                            .show()
                    }

                    else -> binding.lottieLoadingName.visibility = View.GONE
                }
            }
        }
    }

}