package fr.bowser.behaviortracker.do_not_disturbed

import dagger.Module
import dagger.Provides
import fr.bowser.feature_do_not_disturb.DoNotDisturbGraph
import fr.bowser.feature_do_not_disturb.DoNotDisturbManager
import javax.inject.Singleton

@Module
class DoNotDisturbModule {

    @Singleton
    @Provides
    fun provideDoNotDisturbManager(): DoNotDisturbManager {
        return DoNotDisturbGraph.getDoNotDisturbManager()
    }
}
