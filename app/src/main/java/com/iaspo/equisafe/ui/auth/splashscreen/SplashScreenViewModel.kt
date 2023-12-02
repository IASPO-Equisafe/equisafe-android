package com.iaspo.equisafe.ui.auth.splashscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.iaspo.equisafe.core.constant.DataStoreConstant
import com.iaspo.equisafe.core.data.local.UserPreferences

class SplashScreenViewModel(private val dataStore: UserPreferences): ViewModel()  {
    fun getUserSession(): LiveData<String> {
        return  dataStore.getStringData(DataStoreConstant.TOKEN).asLiveData()
    }
}