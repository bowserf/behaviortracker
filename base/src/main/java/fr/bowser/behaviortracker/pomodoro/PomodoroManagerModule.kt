package fr.bowser.behaviortracker.pomodoro

import android.content.Context
import android.os.Vibrator
import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.BuildConfig
import fr.bowser.behaviortracker.setting.SettingManager
import fr.bowser.behaviortracker.timer.TimerManager
import fr.bowser.behaviortracker.timer_repository.TimerRepository
import fr.bowser.behaviortracker.utils.PauseTimer
import javax.inject.Singleton

@Module
class PomodoroManagerModule {

    @Singleton
    @Provides
    fun providePomodoroManager(
        context: Context,
        timeManager: TimerManager,
        timerRepository: TimerRepository,
        settingManager: SettingManager,
    ): PomodoroManager {
        val pauseTimer = PauseTimer.getTimer(context)
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        return PomodoroManagerImpl(
            timeManager,
            timerRepository,
            settingManager,
            pauseTimer,
            vibrator,
            BuildConfig.DEBUG,
        )
    }
}
