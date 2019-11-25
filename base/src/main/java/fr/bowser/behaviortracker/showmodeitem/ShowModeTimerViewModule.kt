package fr.bowser.behaviortracker.showmodeitem

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.timer.TimeManager
import fr.bowser.behaviortracker.utils.GenericScope

@Module
class ShowModeTimerViewModule(private val view: ShowModeTimerViewContract.View) {

    @GenericScope(component = ShowModeTimerViewComponent::class)
    @Provides
    fun provideShowModeTimerViewPresenter(timeManager: TimeManager): ShowModeTimerViewPresenter {
        return ShowModeTimerViewPresenter(view, timeManager)
    }
}