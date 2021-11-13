package fr.bowser.behaviortracker.choose_pomodoro_timer_view

import dagger.Component
import fr.bowser.behaviortracker.config.BehaviorTrackerAppComponent
import fr.bowser.behaviortracker.utils.GenericScope

@GenericScope(component = ChoosePomodoroTimerComponent::class)
@Component(
    modules = [(ChoosePomodoroTimerModule::class)],
    dependencies = [(BehaviorTrackerAppComponent::class)]
)
interface ChoosePomodoroTimerComponent {

    fun inject(dialog: ChoosePomodoroTimerDialog)

    fun provideChoosePomodoroTimerPresenter(): ChoosePomodoroTimerContract.Presenter
}
