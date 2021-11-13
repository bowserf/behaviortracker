package fr.bowser.behaviortracker.showmode

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.timer.TimerListManager
import fr.bowser.behaviortracker.utils.GenericScope

@Module
class ShowModeModule(private val screen: ShowModeContract.Screen) {

    @GenericScope(component = ShowModeComponent::class)
    @Provides
    fun provideShowModePresenter(timerListManager: TimerListManager): ShowModeContract.Presenter {
        return ShowModePresenter(screen, timerListManager)
    }
}
