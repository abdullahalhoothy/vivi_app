package com.app.honey.di.viewmodelscoped

import com.app.honey.domain.repo.LoginRepo
import com.app.honey.data.remote.repo.LoginRepoImpl
import com.app.honey.data.remote.repo.ProductRepoImpl
import com.app.honey.data.remote.repo.SplashRepoImpl
import com.app.honey.domain.interactors.EmailValidationUseCase
import com.app.honey.domain.interactors.PasswordValidationUseCase
import com.app.honey.domain.repo.ProductRepo
import com.app.honey.domain.repo.SplashRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object LoginViewModelScoped {

    @Provides
    @ViewModelScoped
    fun provideLoginRepository(
        loginRepo: LoginRepoImpl
    ): LoginRepo = loginRepo

    @Provides
    @ViewModelScoped
    fun provideProductRepository(
        productRepo: ProductRepoImpl
    ): ProductRepo = productRepo

    @Provides
    @ViewModelScoped
    fun provideSplashRepository(
        splashRepo: SplashRepoImpl
    ): SplashRepo = splashRepo

    @Provides
    @ViewModelScoped
    fun provideEmailValidationUseCase(): EmailValidationUseCase = EmailValidationUseCase()

    @Provides
    @ViewModelScoped
    fun providePasswordValidationUseCase(): PasswordValidationUseCase = PasswordValidationUseCase()
}