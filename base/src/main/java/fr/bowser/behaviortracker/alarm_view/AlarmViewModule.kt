package fr.bowser.behaviortracker.alarm_view

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.event.EventManager
import fr.bowser.behaviortracker.utils.GenericScope
import fr.bowser.feature.alarm.AlarmTimerManager

@Module
class AlarmViewModule(private val screen: AlarmViewContract.Screen) {

    @GenericScope(component = AlarmViewComponent::class)
    @Provides
    fun provideAlarmTimerPresenter(
        alarmTimerManager: AlarmTimerManager,
        eventManager: EventManager,
    ): AlarmViewContract.Presenter {
        return AlarmViewPresenter(screen, alarmTimerManager, eventManager)
    }
}
