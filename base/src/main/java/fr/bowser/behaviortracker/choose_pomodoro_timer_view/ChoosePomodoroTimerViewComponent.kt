package fr.bowser.behaviortracker.choose_pomodoro_timer_view

import dagger.Component
import fr.bowser.behaviortracker.config.BehaviorTrackerAppComponent
import fr.bowser.behaviortracker.utils.GenericScope

@GenericScope(component = ChoosePomodoroTimerViewComponent::class)
@Component(
    modules = [(ChoosePomodoroTimerViewModule::class)],
    dependencies = [(BehaviorTrackerAppComponent::class)]
)
interface ChoosePomodoroTimerViewComponent {

    fun inject(dialog: ChoosePomodoroTimerViewDialog)

    fun provideChoosePomodoroTimerPresenter(): ChoosePomodoroTimerViewContract.Presenter
}
