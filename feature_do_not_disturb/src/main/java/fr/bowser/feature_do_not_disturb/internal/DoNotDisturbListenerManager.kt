package fr.bowser.feature_do_not_disturb.internal

import fr.bowser.feature_do_not_disturb.DoNotDisturbManager

interface DoNotDisturbListenerManager: DoNotDisturbManager {

    fun updateDndState()
}