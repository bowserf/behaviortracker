package fr.bowser.behaviortracker.choosepomodorotimer

import android.content.Context
import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.BuildConfig
import fr.bowser.behaviortracker.pomodoro.PomodoroManager
import fr.bowser.behaviortracker.setting.SettingManager
import fr.bowser.behaviortracker.timer.TimerListManager
import fr.bowser.behaviortracker.utils.GenericScope
import fr.bowser.behaviortracker.utils.PauseTimer

@Module
class ChoosePomodoroTimerModule {

    @GenericScope(component = ChoosePomodoroTimerComponent::class)
    @Provides
    fun provideChoosePomodoroTimerPresenter(context: Context,
                                            timerListManager: TimerListManager,
                                            pomodoroManager: PomodoroManager,
                                            settingManager: SettingManager): ChoosePomodoroTimerPresenter {
        val pauseTimer = PauseTimer.getTimer(context)
        val pomodoroPauseStepDuration = if (BuildConfig.DEBUG) 5L else settingManager.getPomodoroPauseStepDuration()
        val pomodoroStepDuration = if (BuildConfig.DEBUG) 10L else settingManager.getPomodoroStepDuration()
        return ChoosePomodoroTimerPresenter(timerListManager,
                pomodoroManager,
                pomodoroStepDuration,
                pomodoroPauseStepDuration,
                pauseTimer)
    }

}