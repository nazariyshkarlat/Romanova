package com.tma.romanova.di.feature

import com.tma.romanova.presentation.feature.main.view_model.MainScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainScreenModule = module {
    viewModel {
        MainScreenViewModel()
    }
}