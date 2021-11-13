package fr.bowser.behaviortracker.home_view

import android.app.Activity
import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.event.EventManager
import fr.bowser.behaviortracker.instantapp.InstantAppManager
import fr.bowser.behaviortracker.utils.GenericScope

@Module
internal class HomeModule(private val screen: HomeContract.Screen) {

    @GenericScope(component = HomeComponent::class)
    @Provides
    fun provideHomePresenter(
        eventManager: EventManager,
        instantAppManager: InstantAppManager
    ): HomeContract.Presenter {
        return HomePresenter(
            screen,
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
