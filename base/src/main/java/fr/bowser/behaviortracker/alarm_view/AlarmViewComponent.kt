package fr.bowser.behaviortracker.alarm_view

import dagger.Component
import fr.bowser.behaviortracker.config.BehaviorTrackerAppComponent
import fr.bowser.behaviortracker.utils.GenericScope

@GenericScope(component = AlarmViewComponent::class)
@Component(
    modules = [(AlarmViewModule::class)],
    dependencies = [(BehaviorTrackerAppComponent::class)]
)
interface AlarmViewComponent {

    fun inject(timerDialog: AlarmViewDialog)

    fun provideAlarmDialogPresenter(): AlarmViewContract.Presenter
}
