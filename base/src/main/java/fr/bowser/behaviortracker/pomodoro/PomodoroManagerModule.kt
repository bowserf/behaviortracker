package fr.bowser.behaviortracker.pomodoro

import android.content.Context
import android.os.Vibrator
import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.BuildConfig
import fr.bowser.behaviortracker.setting.SettingManager
import fr.bowser.behaviortracker.timer.TimeManager
import fr.bowser.behaviortracker.timer.TimerListManager
import fr.bowser.behaviortracker.utils.PauseTimer
import javax.inject.Singleton

@Module
class PomodoroManagerModule {

    @Singleton
    @Provides
    fun providePomodoroManager(
        context: Context,
        timeManager: TimeManager,
        timerListManager: TimerListManager,
        settingManager: SettingManager
    ): PomodoroManager {
        val pauseTimer = PauseTimer.getTimer(context)
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        return PomodoroManagerImpl(
            timeManager,
            timerListManager,
            settingManager,
            pauseTimer,
            vibrator,
            BuildConfig.DEBUG
        )
    }
}