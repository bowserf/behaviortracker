package fr.bowser.behaviortracker.rewards_view

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.inapp.InAppManager
import fr.bowser.behaviortracker.inapp.InAppRepository
import fr.bowser.behaviortracker.utils.GenericScope

@Module
class RewardsPresenterModule(private val screen: RewardsContract.Screen) {

    @GenericScope(component = RewardsComponent::class)
    @Provides
    fun provideRewardsPresenter(
        inAppRepository: InAppRepository,
        inAppManager: InAppManager
    ): RewardsContract.Presenter {
        return RewardsPresenter(screen, inAppRepository, inAppManager)
    }
}
