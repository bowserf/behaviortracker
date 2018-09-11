package fr.bowser.behaviortracker.rewards

import android.content.Context
import android.content.res.AssetManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal class InAppManagerModule(private val assetManager: AssetManager) {

    @Singleton
    @Provides
    fun provideInAppConfiguration(): InAppConfiguration {
        val inAppConfigurationParser = InAppConfigurationParserImpl(assetManager)
        return InAppConfigurationImpl(inAppConfigurationParser)
    }

    @Singleton
    @Provides
    fun provideInAppRepository(context: Context, inAppConfiguration: InAppConfiguration): InappRepository {
        val sharedPreferences = context.getSharedPreferences(
                InAppRepositoryImpl.SHARED_PREF_KEY,
                Context.MODE_PRIVATE)
        return InAppRepositoryImpl(sharedPreferences, inAppConfiguration)
    }

    @Singleton
    @Provides
    fun provideInAppManager(context: Context,
                            inAppConfiguration: InAppConfiguration,
                            inappRepository: InappRepository): InAppManager {
        val playBillingManager = PlayBillingManager(context)
        return InAppManagerImpl(playBillingManager, inAppConfiguration, inappRepository)
    }

}