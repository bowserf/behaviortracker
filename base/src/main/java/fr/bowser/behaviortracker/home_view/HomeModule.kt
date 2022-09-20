package fr.bowser.behaviortracker.home_view

import android.app.Activity
import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.event.EventManager
import fr.bowser.behaviortracker.utils.GenericScope

@Module
internal class HomeModule(private val screen: HomeContract.Screen, private val activity: Activity) {

    @GenericScope(component = HomeComponent::class)
    @Provides
    fun provideHomePresenter(
        eventManager: EventManager
    ): HomeContract.Presenter {
        return HomePresenter(
            screen,
            eventManager
        )
    }
}
