package fr.bowser.behaviortracker.home_activity

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.event.EventManager
import fr.bowser.behaviortracker.navigation.NavigationManager
import fr.bowser.behaviortracker.utils.GenericScope

@Module
internal class HomeActivityModule(
    private val screen: HomeActivityContract.Screen,
) {

    @GenericScope(component = HomeActivityComponent::class)
    @Provides
    fun provideHomePresenter(
        eventManager: EventManager,
        navigationManager: NavigationManager,
    ): HomeActivityContract.Presenter {
        return HomeActivityPresenter(
            screen,
            eventManager,
            navigationManager,
        )
    }
}
