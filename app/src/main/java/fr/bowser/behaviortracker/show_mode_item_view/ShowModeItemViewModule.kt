package fr.bowser.behaviortracker.show_mode_item_view

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.timer.TimerManager
import fr.bowser.behaviortracker.utils.GenericScope

@Module
class ShowModeItemViewModule(private val screen: ShowModeItemViewContract.Screen) {

    @GenericScope(component = ShowModeItemViewComponent::class)
    @Provides
    fun provideShowModeTimerViewPresenter(
        timeManager: TimerManager,
    ): ShowModeItemViewContract.Presenter {
        return ShowModeItemViewPresenter(screen, timeManager)
    }
}
