package fr.bowser.behaviortracker.inapp

import android.content.Context
import dagger.Module
import dagger.Provides
import fr.bowser.feature.billing.InAppManager
import fr.bowser.feature.billing.InAppModule
import javax.inject.Singleton

@Module
class InAppManagerModule {

    @Singleton
    @Provides
    fun provideInAppConfiguration(context: Context): InAppConfiguration {
        val inAppConfigurationParser = InAppConfigurationParserImpl(context.assets)
        return InAppConfigurationImpl(inAppConfigurationParser)
    }

    @Singleton
    @Provides
    fun provideInAppManager(
        context: Context
    ): InAppManager {
        return InAppModule(context).createInAppManager()
    }
}
