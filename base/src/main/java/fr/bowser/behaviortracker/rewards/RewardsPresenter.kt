package fr.bowser.behaviortracker.rewards

import com.android.billingclient.api.Purchase
import fr.bowser.behaviortracker.inapp.InAppManager
import fr.bowser.behaviortracker.inapp.InAppRepository

class RewardsPresenter(
    private val screen: RewardsContract.Screen,
    private val inAppRepository: InAppRepository,
    private val inAppManager: InAppManager
) : RewardsContract.Presenter {

    private val inAppListener: InAppManager.Listener

    init {
        inAppListener = createInAppManagerListener()
    }

    override fun onStart() {
        inAppManager.addListener(inAppListener)

        val inApps = inAppRepository.getInApps()
        screen.displayListInApps(inApps)
    }

    override fun onStop() {
        inAppManager.removeListener(inAppListener)
    }

    private fun createInAppManagerListener(): InAppManager.Listener {
        return object : InAppManager.Listener {
            override fun onPurchaseSucceed(purchases: List<Purchase>) {
                screen.displaySuccessPurchaseMessage()
            }

            override fun onPurchaseFailed() {
                screen.displayFailPurchaseMessage()
            }
        }
    }
}
