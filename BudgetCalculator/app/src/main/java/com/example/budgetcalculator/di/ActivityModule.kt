package com.example.budgetcalculator.di

import android.app.Activity
import com.example.budgetcalculator.ui.main.MainActivity
import com.example.budgetcalculator.ui.main.MainContract
import com.example.budgetcalculator.ui.main.MainModel
import com.example.budgetcalculator.ui.main.MainPresenter
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@InstallIn(ActivityComponent::class)
@Module
abstract class ActivityModule {

    @Binds
    abstract fun bindActivity(activity: MainActivity) : MainContract.View

    @Binds
    abstract fun bindPresenter(presenter: MainPresenter) : MainContract.Presenter

    @Binds
    abstract fun bindModel(model: MainModel): MainContract.Model

}

@InstallIn(ActivityComponent::class)
@Module
object MainActivityModule {

    @Provides
    fun bindActivity(activity: Activity): MainActivity {
        return activity as MainActivity
    }
}