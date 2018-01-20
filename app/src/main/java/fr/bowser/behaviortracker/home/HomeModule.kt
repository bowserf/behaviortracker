package fr.bowser.behaviortracker.home

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal class HomeModule(private val homeView: HomeContract.View) {

    @Singleton
    @Provides
    fun provideHomePresenter(): HomePresenter {
        return HomePresenter(homeView)
    }

}/* package */
