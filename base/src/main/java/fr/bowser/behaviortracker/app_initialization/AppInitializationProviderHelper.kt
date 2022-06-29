package fr.bowser.behaviortracker.app_initialization

import android.content.Context

object AppInitializationProviderHelper {

    private const val PROVIDER_CLASS_INSTALLED_APP =
        "fr.bowser.behaviortracker.app.app_initialization.AppInitializationComponentProvider"

    private const val PROVIDER_CLASS_INSTANT_APP =
        "fr.bowser.behaviortracker.instantapp.app_initialization.AppInitializationComponentProvider"

    fun provideAppInitializationComponent(context: Context, isInstantApp: Boolean): AppInitializationComponent {
        val providerClass =
            if (isInstantApp) PROVIDER_CLASS_INSTANT_APP else PROVIDER_CLASS_INSTALLED_APP
        try {
            val clz: Class<*> = Class.forName(providerClass)
            val field = clz.getDeclaredField("INSTANCE")
            val instance = field.get(null)
            val provider = instance as AppInitializationProvider
            return provider.provideAppInitializationComponent(context)
        } catch (e: ClassNotFoundException) {
            throw IllegalStateException("AppInitializationComponent provider class not found: ${e.message}")
        }
    }
}
