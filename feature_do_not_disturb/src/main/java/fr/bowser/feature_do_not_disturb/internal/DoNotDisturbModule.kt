package fr.bowser.feature_do_not_disturb.internal

internal class DoNotDisturbModule {

    fun createDoNotDisturbManager(): DoNotDisturbListenerManager {
        val context = DoNotDisturbGraphInternal.getContext()
        return DoNotDisturbManagerImpl(
            context
        )
    }
}
