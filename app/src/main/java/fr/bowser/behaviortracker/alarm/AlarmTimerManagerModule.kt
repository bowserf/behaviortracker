package fr.bowser.behaviortracker.alarm

import android.content.Context
import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.BuildConfig
import javax.inject.Singleton

@Module
class AlarmTimerManagerModule {

    @Singleton
    @Provides
    fun provideAlarmStorageManager(context: Context): AlarmStorageManager {
        if(BuildConfig.UA){
            return AlarmStorageManagerUA()
        }else {
            return AlarmStorageManagerImpl(context)
        }
    }

    @Singleton
    @Provides
    fun provideAlarmTimerManager(context: Context, alarmStorageManager: AlarmStorageManager): AlarmTimerManager {
        return AlarmTimerManagerImpl(context, alarmStorageManager)
    }

}