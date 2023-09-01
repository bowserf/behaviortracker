package fr.bowser.behaviortracker.pomodoro_choose_timer_view

import dagger.Component
import fr.bowser.behaviortracker.config.BehaviorTrackerAppComponent
import fr.bowser.behaviortracker.utils.GenericScope

@GenericScope(component = PomodoroChooseTimerViewComponent::class)
@Component(
    modules = [(PomodoroChooseTimerViewModule::class)],
    dependencies = [(BehaviorTrackerAppComponent::class)]
)
interface PomodoroChooseTimerViewComponent {

    fun inject(dialog: PomodoroChooseTimerViewDialog)

    fun providePomodoroChooseTimerPresenter(): PomodoroChooseTimerViewContract.Presenter
}
