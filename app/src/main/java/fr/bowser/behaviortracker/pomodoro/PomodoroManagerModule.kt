package fr.bowser.behaviortracker.pomodoro

import android.content.Context
import android.os.Vibrator
import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.setting.SettingManager
import fr.bowser.behaviortracker.timer.TimeManager
import javax.inject.Singleton


@Module
class PomodoroManagerModule {

    @Singleton
    @Provides
    fun providePomodoroManager(context: Context,
                               timeManager: TimeManager,
                               settingManager: SettingManager): PomodoroManager {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        return PomodoroManagerImpl(timeManager, settingManager, vibrator)
    }

}