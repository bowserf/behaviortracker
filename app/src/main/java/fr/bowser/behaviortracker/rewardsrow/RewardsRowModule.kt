package fr.bowser.behaviortracker.rewardsrow

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.event.EventManager
import fr.bowser.behaviortracker.inapp.InAppManager
import fr.bowser.behaviortracker.utils.GenericScope

@Module
class RewardsRowModule(private val screen: RewardsRowContract.Screen) {

    @GenericScope(component = RewardsRowComponent::class)
    @Provides
    fun provideRewardsRowPresenter(inAppManager: InAppManager,
                                   eventManager: EventManager): RewardsRowPresenter {
        return RewardsRowPresenter(screen, inAppManager, eventManager)
    }

}