package fr.bowser.behaviortracker.home

import android.app.Activity
import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.event.EventManager
import fr.bowser.behaviortracker.instantapp.InstantAppManager
import fr.bowser.behaviortracker.notification.TimerNotificationManager
import fr.bowser.behaviortracker.utils.GenericScope

@Module
internal class HomeModule(private val screen: HomeContract.Screen) {

    @GenericScope(component = HomeComponent::class)
    @Provides
    fun provideHomePresenter(
        timerNotificationManager: TimerNotificationManager,
        eventManager: EventManager,
        instantAppManager: InstantAppManager
    ): HomeContract.Presenter {
        return HomePresenter(
            screen,
            timerNotificationManager,
            eventManager,
            instantAppManager,
            createInstallAppAddon(instantAppManager)
        )
    }

    private fun createInstallAppAddon(instantAppManagerHelper: InstantAppManager): HomePresenter.InstantAppAddon {
        return object : HomePresenter.InstantAppAddon {
            override fun displayInstallAppDialog() {
                val activity = screen as Activity
                instantAppManagerHelper.showInstallPrompt(activity)
            }
        }
    }
}