package fr.bowser.behaviortracker.alarm

import dagger.Module
import dagger.Provides
import fr.bowser.feature.alarm.AlarmGraph
import fr.bowser.feature.alarm.AlarmTimerManager
import javax.inject.Singleton

@Module
class AlarmTimerManagerModule {

    @Singleton
    @Provides
    fun provideAlarmTimerManager(): AlarmTimerManager {
        return AlarmGraph.getAlarmTimerManager()
    }
}
