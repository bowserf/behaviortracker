package fr.bowser.behaviortracker.alarm

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.event.EventManager
import fr.bowser.behaviortracker.utils.GenericScope
import fr.bowser.feature.alarm.AlarmTimerManager

@Module
class AlarmTimerModule(private val screen: AlarmTimerContract.Screen) {

    @GenericScope(component = AlarmTimerComponent::class)
    @Provides
    fun provideAlarmTimerPresenter(
        alarmTimerManager: AlarmTimerManager,
        eventManager: EventManager
    ): AlarmTimerContract.Presenter {
        return AlarmDialogPresenter(screen, alarmTimerManager, eventManager)
    }
}