package com.tma.romanova.di.feature

import com.tma.romanova.presentation.feature.onboarding.view_model.OnBoardingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val onBoardingModule = module {
    viewModel {
        OnBoardingViewModel()
    }
}