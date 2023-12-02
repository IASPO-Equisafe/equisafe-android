package com.iaspo.equisafe.di

import com.iaspo.equisafe.core.domain.usecase.AccountInteractor
import com.iaspo.equisafe.core.domain.usecase.AccountUseCase
import com.iaspo.equisafe.core.domain.usecase.AuthenticationInteractor
import com.iaspo.equisafe.core.domain.usecase.AuthenticationUseCase
import com.iaspo.equisafe.core.domain.usecase.EmergencyInteractor
import com.iaspo.equisafe.core.domain.usecase.EmergencyUseCase
import com.iaspo.equisafe.core.domain.usecase.HomeInteractor
import com.iaspo.equisafe.core.domain.usecase.HomeUseCase
import com.iaspo.equisafe.core.domain.usecase.LearningInteractor
import com.iaspo.equisafe.core.domain.usecase.LearningUseCase
import com.iaspo.equisafe.ui.auth.login.LoginViewModel
import com.iaspo.equisafe.ui.auth.register.RegisterViewModel
import com.iaspo.equisafe.ui.auth.splashscreen.SplashScreenViewModel
import com.iaspo.equisafe.ui.emergency.EmergencyViewModel
import com.iaspo.equisafe.ui.home.HomeViewModel
import com.iaspo.equisafe.ui.learning.LearningViewModel
import com.iaspo.equisafe.ui.map.MapViewModel
import com.iaspo.equisafe.ui.profile.ProfileViewModel
import com.iaspo.equisafe.ui.profile.changepassword.ChangePasswordViewModel
import com.iaspo.equisafe.ui.profile.editprofile.EditProfileViewModel
import com.iaspo.equisafe.ui.profile.help.HelpViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val userCaseModule = module {
    factory<AuthenticationUseCase> { AuthenticationInteractor(get()) }
    factory<HomeUseCase> { HomeInteractor(get()) }
    factory<AccountUseCase> { AccountInteractor(get()) }
    factory<EmergencyUseCase> { EmergencyInteractor(get()) }
    factory<LearningUseCase> { LearningInteractor(get()) }
}

val viewModelModule = module {
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { SplashScreenViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { EditProfileViewModel(get()) }
    viewModel { ChangePasswordViewModel(get()) }
    viewModel { HelpViewModel() }
    viewModel { EmergencyViewModel(get()) }
    viewModel { LearningViewModel(get()) }
    viewModel { MapViewModel(get()) }
}