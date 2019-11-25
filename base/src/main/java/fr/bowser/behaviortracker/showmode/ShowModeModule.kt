package fr.bowser.behaviortracker.showmode

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.timer.TimerListManager
import fr.bowser.behaviortracker.utils.GenericScope

@Module
class ShowModeModule(private val view: ShowModeContract.View) {

    @GenericScope(component = ShowModeComponent::class)
    @Provides
    fun provideShowModePresenter(timerListManager: TimerListManager): ShowModePresenter {
        return ShowModePresenter(view, timerListManager)
    }
}