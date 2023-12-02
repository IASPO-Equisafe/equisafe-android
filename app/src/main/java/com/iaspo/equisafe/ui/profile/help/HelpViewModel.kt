package com.iaspo.equisafe.ui.profile.help

import androidx.lifecycle.ViewModel
import com.iaspo.equisafe.core.constant.ContactConstant

class HelpViewModel(): ViewModel() {
    val equisafeCall = ContactConstant.EQUISAFE_CALL
    val equisafeSMS = ContactConstant.EQUISAFE_SMS
    val equisafeWhatsApp = ContactConstant.EQUISAFE_WHATSAPP
    val equisafeEmail = ContactConstant.EQUISAFE_EMAIL
}