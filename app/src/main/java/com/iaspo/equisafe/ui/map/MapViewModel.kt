package com.iaspo.equisafe.ui.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.iaspo.equisafe.core.domain.usecase.HomeUseCase

class MapViewModel(homeUseCase: HomeUseCase): ViewModel() {

    val getMap = homeUseCase.getMap().asLiveData()
}