package com.tma.romanova.presentation.feature.onboarding.view_model

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.tma.romanova.core.metadata
import com.tma.romanova.domain.feature.metadata.Metadata
import com.tma.romanova.domain.navigation.NavigationDirections
import com.tma.romanova.domain.navigation.NavigationManager
import com.tma.romanova.presentation.R
import com.tma.romanova.presentation.extensions.drawable
import com.tma.romanova.presentation.extensions.str
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.android.ext.android.inject
import org.koin.core.context.GlobalContext

class OnBoardingViewModel : ViewModel() {

    companion object{
        val items =
            listOf(
                OnBoardingItem(
                    titleRes = R.string.on_boarding_first,
                    imageRes = R.drawable.onboarding_first_image
                ),
                OnBoardingItem(
                    titleRes = R.string.on_boarding_second,
                    imageRes = R.drawable.onboarding_second_image
                )
            )
    }

    val isLastItemSelected
    get() = selectedItem.value == lastItem

    val lastItem
    get() = items.last()

    val nextItem
    get() = if(currentIndex+1 <= items.lastIndex) items[currentIndex+1] else items[currentIndex]

    val currentIndex
    get() = items.indexOf(selectedItem.value)

    val selectedItem: StateFlow<OnBoardingUi> by lazy{
        _selectedItem
    }

    val items =
        Companion.items.map {
            it.toUi()
        }

    private val _selectedItem =
        MutableStateFlow<OnBoardingUi>(
            items.first()
        )

    fun notifyItemSelected(item: OnBoardingUi){
        _selectedItem.value = item
    }

    fun notifyOnBoardingCompleted(){
        NavigationManager.navigate(
            directions = NavigationDirections.MainScreen.create()
        )
        metadata.rememberOnBoardingCompletion()
    }

    data class OnBoardingUi(
        val title: String,
        val image: Drawable
    )

    data class OnBoardingItem(
        @StringRes val titleRes: Int,
        @DrawableRes val imageRes: Int
    ){
        fun toUi() = OnBoardingUi(
            title = titleRes.str,
            image = imageRes.drawable
        )
    }

}