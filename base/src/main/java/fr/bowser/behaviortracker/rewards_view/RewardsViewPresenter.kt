package fr.bowser.behaviortracker.rewards_view

import fr.bowser.behaviortracker.inapp.InAppConfiguration
import fr.bowser.feature.billing.InAppManager

class RewardsViewPresenter(
    private val screen: RewardsViewContract.Screen,
    private val inAppConfiguration: InAppConfiguration,
    private val inAppManager: InAppManager
) : RewardsViewContract.Presenter {

    private val inAppListener: InAppManager.Listener

    init {
        inAppListener = createInAppManagerListener()
    }

    override fun onStart() {
        inAppManager.addListener(inAppListener)

        val inApps = inAppConfiguration.getInApps()
        screen.displayListInApps(inApps)
    }

    override fun onStop() {
        inAppManager.removeListener(inAppListener)
    }

    private fun createInAppManagerListener(): InAppManager.Listener {
        return object : InAppManager.Listener {
            override fun onPurchaseSucceed(purchases: List<InAppManager.StorePurchase>) {
                screen.displaySuccessPurchaseMessage()
            }

            override fun onPurchaseFailed() {
                screen.displayFailPurchaseMessage()
            }
        }
    }
}
