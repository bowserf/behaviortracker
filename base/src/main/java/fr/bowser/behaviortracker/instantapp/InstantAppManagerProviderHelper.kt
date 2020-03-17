package fr.bowser.behaviortracker.instantapp

object InstantAppManagerProviderHelper {

    private const val PROVIDER_CLASS_INSTALLED_APP =
        "fr.bowser.behaviortracker.app.instantapp.InstantAppComponentProvider"

    private const val PROVIDER_CLASS_INSTANT_APP =
        "fr.bowser.behaviortracker.instantapp.instantapp.InstantAppComponentProvider"

    fun provideMyInstantAppComponent(isInstantApp: Boolean): InstantAppComponent {
        val providerClass =
            if (isInstantApp) PROVIDER_CLASS_INSTANT_APP else PROVIDER_CLASS_INSTALLED_APP
        try {
            /*val provider = Class.forName(providerClass).kotlin.objectInstance as InstantAppManagerProvider
            return provider.provideMyInstantAppComponent()*/

            val clz: Class<*> = Class.forName(providerClass)
            val field = clz.getDeclaredField("INSTANCE")
            val instance = field.get(null)
            val provider = instance as InstantAppManagerProvider
            return provider.provideMyInstantAppComponent()
        } catch (e: ClassNotFoundException) {
            throw IllegalStateException("InstantAppComponent provider class not found: ${e.message}")
        }
    }
}