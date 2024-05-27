package fr.bowser.behaviortracker.crash_record

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CrashRecordModule {

    @Singleton
    @Provides
    fun provideCrashRecordManager(
        context: Context,
    ): CrashRecordManager {
        val sharedPreferences = context.getSharedPreferences(
            CrashRecordManagerImpl.PREFERENCES_NAME,
            Context.MODE_PRIVATE,
        )
        return CrashRecordManagerImpl(context, sharedPreferences)
    }
}
