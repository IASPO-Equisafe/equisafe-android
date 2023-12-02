package com.iaspo.equisafe.ui.profile.help

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.iaspo.equisafe.R
import com.iaspo.equisafe.databinding.ActivityHelpBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class HelpActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHelpBinding
    private val helpViewModel: HelpViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val window: Window? = this.window
        window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window?.statusBarColor = ContextCompat.getColor(this, R.color.green_main)

        binding = ActivityHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun initView() {
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnHelpCall.text = this.getString(R.string.help_string, helpViewModel.equisafeCall)
        binding.btnHelpEmail.text = this.getString(R.string.help_string, helpViewModel.equisafeEmail)
        binding.btnHelpSms.text = this.getString(R.string.help_string, helpViewModel.equisafeSMS)
        binding.btnHelpWa.text = this.getString(R.string.help_string, helpViewModel.equisafeWhatsApp)

        binding.btnHelpEmail.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, helpViewModel.equisafeEmail)
                putExtra(Intent.EXTRA_SUBJECT, "Need Help")
            }

            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
        }

        binding.btnHelpWa.setOnClickListener {
            val url = "https://api.whatsapp.com/send?phone=${helpViewModel.equisafeWhatsApp}&text=I Need Help"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        binding.btnHelpCall.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:${helpViewModel.equisafeCall}")
            }
            startActivity(intent)
        }

        binding.btnHelpSms.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("smsto:${helpViewModel.equisafeCall}")
                putExtra("sms_body", "I Need Help")
            }
            startActivity(intent)
        }
    }
}