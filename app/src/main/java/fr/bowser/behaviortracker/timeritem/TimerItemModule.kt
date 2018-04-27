package fr.bowser.behaviortracker.timeritem

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.notification.TimerNotificationManager
import fr.bowser.behaviortracker.setting.SettingManager
import fr.bowser.behaviortracker.timer.TimeManager
import fr.bowser.behaviortracker.timer.TimerListManager
import fr.bowser.behaviortracker.utils.GenericScope

@Module
class TimerItemModule(private val view: TimerItemContract.View) {

    @GenericScope(component = TimerItemComponent::class)
    @Provides

    fun provideTimerItemPresenter(timeManager: TimeManager,
                                  timerListManager: TimerListManager,
                                  timerNotificationManager: TimerNotificationManager,
                                  settingManager: SettingManager): TimerItemPresenter {
        return TimerItemPresenter(
                view,
                timeManager,
                timerListManager,
                timerNotificationManager,
                settingManager)
    }

}