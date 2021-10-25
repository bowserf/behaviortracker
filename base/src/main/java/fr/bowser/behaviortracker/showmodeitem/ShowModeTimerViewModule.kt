package fr.bowser.behaviortracker.showmodeitem

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.timer.TimeManager
import fr.bowser.behaviortracker.utils.GenericScope

@Module
class ShowModeTimerViewModule(private val screen: ShowModeTimerViewContract.Screen) {

    @GenericScope(component = ShowModeTimerViewComponent::class)
    @Provides
    fun provideShowModeTimerViewPresenter(
        timeManager: TimeManager
    ): ShowModeTimerViewContract.Presenter {
        return ShowModeTimerViewPresenter(screen, timeManager)
    }
}