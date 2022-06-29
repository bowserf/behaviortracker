package fr.bowser.feature.billing

import android.content.Context
import fr.bowser.feature.billing.internal.InAppManagerImpl
import fr.bowser.feature.billing.internal.PlayBillingManager

class InAppModule(
    private val context: Context
) {

    fun createInAppManager(): InAppManager {
        val playBillingManager = PlayBillingManager(context)
        return InAppManagerImpl(playBillingManager)
    }
}
