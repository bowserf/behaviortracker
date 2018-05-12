package fr.bowser.behaviortracker.pomodoro

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.timer.TimeManager
import javax.inject.Singleton

@Module
class PomodoroManagerModule {

    @Singleton
    @Provides
    fun providePomodoroManager(timeManager: TimeManager): PomodoroManager {
        return PomodoroManager(timeManager)
    }

}