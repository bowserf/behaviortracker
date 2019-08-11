package fr.bowser.behaviortracker.home

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.event.EventManager
import fr.bowser.behaviortracker.notification.TimerNotificationManager
import fr.bowser.behaviortracker.utils.GenericScope

@Module
internal class HomeModule {

    @GenericScope(component = HomeComponent::class)
    @Provides
    fun provideHomePresenter(
        timerNotificationManager: TimerNotificationManager,
        eventManager: EventManager
    ): HomePresenter {
        return HomePresenter(
            timerNotificationManager,
            eventManager
        )
    }
}