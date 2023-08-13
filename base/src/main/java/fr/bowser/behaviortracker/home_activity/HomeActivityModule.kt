package fr.bowser.behaviortracker.home_activity

import android.app.Activity
import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.event.EventManager
import fr.bowser.behaviortracker.instantapp.InstantAppManager
import fr.bowser.behaviortracker.utils.GenericScope

@Module
internal class HomeActivityModule(
    private val screen: HomeActivityContract.Screen,
    private val activity: Activity
) {

    @GenericScope(component = HomeActivityComponent::class)
    @Provides
    fun provideHomePresenter(
        eventManager: EventManager,
        instantAppManager: InstantAppManager
    ): HomeActivityContract.Presenter {
        return HomeActivityPresenter(
            screen,
            eventManager,
            instantAppManager,
            createInstallAppAddon(instantAppManager)
        )
    }

    private fun createInstallAppAddon(instantAppManagerHelper: InstantAppManager): HomeActivityPresenter.InstantAppAddon {
        return object : HomeActivityPresenter.InstantAppAddon {
            override fun displayInstallAppDialog() {
                instantAppManagerHelper.showInstallPrompt(activity)
            }
        }
    }
}
