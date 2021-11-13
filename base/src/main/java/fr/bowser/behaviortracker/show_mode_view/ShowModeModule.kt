package fr.bowser.behaviortracker.show_mode_view

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.timer_list.TimerListManager
import fr.bowser.behaviortracker.utils.GenericScope

@Module
class ShowModeModule(private val screen: ShowModeContract.Screen) {

    @GenericScope(component = ShowModeComponent::class)
    @Provides
    fun provideShowModePresenter(timerListManager: TimerListManager): ShowModeContract.Presenter {
        return ShowModePresenter(screen, timerListManager)
    }
}
