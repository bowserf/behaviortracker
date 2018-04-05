package fr.bowser.behaviortracker.timerlist

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.notification.TimerNotificationManager
import fr.bowser.behaviortracker.timer.TimerListManager
import fr.bowser.behaviortracker.utils.GenericScope

@Module
class TimerModule(private val timerView: TimerContract.View) {

    @GenericScope(component = TimerComponent::class)
    @Provides
    fun provideTimerPresenter(timerListManager: TimerListManager,
                              timerNotificationManager: TimerNotificationManager): TimerPresenter {
        return TimerPresenter(timerView,
                timerListManager,
                timerNotificationManager)
    }

}
