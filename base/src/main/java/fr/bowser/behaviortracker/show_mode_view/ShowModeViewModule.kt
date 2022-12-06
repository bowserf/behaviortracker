package fr.bowser.behaviortracker.show_mode_view

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.timer_list.TimerListManager
import fr.bowser.behaviortracker.utils.GenericScope

@Module
class ShowModeViewModule(private val screen: ShowModeViewContract.Screen) {

    @GenericScope(component = ShowModeViewComponent::class)
    @Provides
    fun provideShowModePresenter(timerListManager: TimerListManager): ShowModeViewContract.Presenter {
        return ShowModeViewPresenter(screen, timerListManager)
    }
}
