package fr.bowser.behaviortracker.rewards_view

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.inapp.InAppConfiguration
import fr.bowser.behaviortracker.utils.GenericScope
import fr.bowser.feature.billing.InAppManager

@Module
class RewardsPresenterModule(private val screen: RewardsContract.Screen) {

    @GenericScope(component = RewardsComponent::class)
    @Provides
    fun provideRewardsPresenter(
        inAppConfiguration: InAppConfiguration,
        inAppManager: InAppManager
    ): RewardsContract.Presenter {
        return RewardsPresenter(screen, inAppConfiguration, inAppManager)
    }
}
