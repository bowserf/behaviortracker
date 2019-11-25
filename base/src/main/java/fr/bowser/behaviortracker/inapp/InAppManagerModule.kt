package fr.bowser.behaviortracker.inapp

import android.content.Context
import dagger.Module
import dagger.Provides
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
    fun provideInAppRepository(
        context: Context,
        inAppConfiguration: InAppConfiguration
    ): InAppRepository {
        val sharedPreferences = context.getSharedPreferences(
            InAppRepositoryImpl.SHARED_PREF_KEY,
            Context.MODE_PRIVATE
        )
        return InAppRepositoryImpl(sharedPreferences, inAppConfiguration.getInApps())
    }

    @Singleton
    @Provides
    fun provideInAppManager(
        context: Context,
        inAppConfiguration: InAppConfiguration,
        inAppRepository: InAppRepository
    ): InAppManager {
        val playBillingManager = PlayBillingManager(context)
        return InAppManagerImpl(playBillingManager, inAppConfiguration, inAppRepository)
    }
}