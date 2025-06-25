package fr.bowser.behaviortracker.show_mode_view

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.timer_repository.TimerRepository
import fr.bowser.behaviortracker.utils.GenericScope

@Module
class ShowModeViewModule(private val screen: ShowModeViewContract.Screen) {

    @GenericScope(component = ShowModeViewComponent::class)
    @Provides
    fun provideShowModePresenter(timerRepository: TimerRepository): ShowModeViewContract.Presenter {
        return ShowModeViewPresenter(screen, timerRepository)
    }
}
