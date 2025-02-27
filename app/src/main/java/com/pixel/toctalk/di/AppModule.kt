package com.pixel.toctalk.di

import com.pixel.toctalk.auth.data.AuthenticationFirebaseDataSource
import com.pixel.toctalk.auth.domain.AuthenticationRepository
import com.pixel.toctalk.auth.domain.usecase.ValidationUseCase
import com.pixel.toctalk.auth.presentation.fragment.createAccount.RegisterViewModel
import com.pixel.toctalk.auth.presentation.fragment.login.LoginViewModel
import com.pixel.toctalk.auth.presentation.fragment.resetPassword.ResetPasswordViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule =
    module {
        singleOf(::AuthenticationFirebaseDataSource) { bind<AuthenticationRepository>() }
        factoryOf(::ValidationUseCase)

        viewModelOf(::LoginViewModel)
        viewModelOf(::ResetPasswordViewModel)
        viewModelOf(::RegisterViewModel)
    }
