package com.iaspo.equisafe.ui.emergency

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.iaspo.equisafe.R
import com.iaspo.equisafe.core.data.network.request.ChangeEmergencyRequest
import com.iaspo.equisafe.core.data.network.retrofit.ApiResult
import com.iaspo.equisafe.core.utils.gone
import com.iaspo.equisafe.core.utils.invisible
import com.iaspo.equisafe.core.utils.visible
import com.iaspo.equisafe.databinding.FragmentEmergencyBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel

class EmergencyFragment : Fragment() {

    private var _binding: FragmentEmergencyBinding? = null
    private val binding get() = _binding!!

    private val emergencyViewModel: EmergencyViewModel by viewModel()

    private var phoneNumberEmergency: String? = null
    private var nameEmergency: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmergencyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setupAction()
    }

    private fun setupAction() {

        binding.btnEditEmergency.setOnClickListener {
            doShow()
        }

        binding.btnCall.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$phoneNumberEmergency")
            }
            startActivity(intent)
        }

        binding.layoutSetEmergency.btnClose.setOnClickListener {
            doHide()
        }

        binding.layoutSetEmergency.bgChangeAddress.setOnClickListener {
            doHide()
        }

        binding.layoutSetEmergency.btnSetEmergency.setOnClickListener {
            val emergencyNameValue = binding.layoutSetEmergency.inputRegisterContactName.text.toString().trim()
            val emergencyNumberValue = binding.layoutSetEmergency.inputRegisterContactNumber.text.toString().trim()

            val changeEmergencyRequest = ChangeEmergencyRequest(
                name = emergencyNameValue,
                telp = emergencyNumberValue
            )

            emergencyViewModel.changeEmergency(changeEmergencyRequest).observe(viewLifecycleOwner) {
                it?.let { response ->
                    when(response) {
                        is ApiResult.Loading -> {
                            binding.layoutSetEmergency.lottieLoading.visible()
                            binding.layoutSetEmergency.btnSetEmergency.invisible()
                        }

                        is ApiResult.Success -> {
                            binding.layoutSetEmergency.lottieLoading.gone()
                            binding.layoutSetEmergency.btnSetEmergency.visible()
                            dataPass(emergencyNameValue, emergencyNumberValue)
                            Toast.makeText(requireContext(), response.data.message, Toast.LENGTH_LONG).show()
                        }

                        is ApiResult.Error -> {
                            binding.layoutSetEmergency.lottieLoading.gone()
                            binding.layoutSetEmergency.btnSetEmergency.visible()
                            Toast.makeText(requireContext(), response.errorMessage, Toast.LENGTH_LONG).show()
                        }

                        else -> {
                            binding.layoutSetEmergency.lottieLoading.gone()
                            binding.layoutSetEmergency.btnSetEmergency.visible()
                        }
                    }
                }
            }
        }
    }

    private fun doShow() {
        val bgEmergency = binding.layoutSetEmergency.bgChangeAddress
        val containerEmergency = binding.layoutSetEmergency.containerSetEmergency

        binding.layoutSetEmergency.btnSetEmergency.isEnabled = true

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

        if (!nameEmergency.isNullOrEmpty() && !phoneNumberEmergency.isNullOrEmpty()) {
            binding.layoutSetEmergency.inputRegisterContactName.setText(nameEmergency)
            binding.layoutSetEmergency.inputRegisterContactNumber.setText(phoneNumberEmergency)
        }
    }
    private fun dataPass(emergencyNameValue: String, emergencyNumberValue: String) {
        binding.tvEmergencyName.text = emergencyNameValue
        binding.tvEmergencyNumber.text = getString(R.string.emergencynumber, emergencyNumberValue)
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
    }

    private fun doGone() {
        val bgEmergency = binding.layoutSetEmergency.bgChangeAddress
        val containerEmergency = binding.layoutSetEmergency.containerSetEmergency

        bgEmergency.visibility = View.GONE
        containerEmergency.visibility = View.GONE
    }

    private fun initView() {
        binding.layoutSetEmergency.tvSortTitle.text = getString(R.string.edit_emergency_contact)

        emergencyViewModel.getEmergencyName().observe(viewLifecycleOwner){
            it?.let { name ->
                binding.tvEmergencyName.text = name
                nameEmergency = name
            }
        }

        emergencyViewModel.getEmergencyNumber().observe(viewLifecycleOwner){
            it?.let { number ->
                binding.tvEmergencyNumber.text = getString(R.string.phone_numebr, number)
                phoneNumberEmergency = "0$number"
            }
        }
    }
}