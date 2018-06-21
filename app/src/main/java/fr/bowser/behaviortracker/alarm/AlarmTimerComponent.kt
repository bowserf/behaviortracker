package fr.bowser.behaviortracker.alarm

import dagger.Component
import fr.bowser.behaviortracker.config.BehaviorTrackerAppComponent
import fr.bowser.behaviortracker.utils.GenericScope

@GenericScope(component = AlarmTimerComponent::class)
@Component(
        modules = [(AlarmTimerModule::class)],
        dependencies = [(BehaviorTrackerAppComponent::class)])
interface AlarmTimerComponent {

    fun inject(timerDialog: AlarmTimerDialog)

    fun provideAlarmDialogPresenter(): AlarmDialogPresenter

}