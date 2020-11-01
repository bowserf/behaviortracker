package fr.bowser.behaviortracker.rewardsrow

import android.app.Activity
import com.android.billingclient.api.SkuDetails
import fr.bowser.behaviortracker.event.EventManager
import fr.bowser.behaviortracker.inapp.InAppManager

class RewardsRowPresenter(
    private val screen: RewardsRowContract.Screen,
    private val inAppManager: InAppManager,
    private val eventManager: EventManager
) : RewardsRowContract.Presenter {

    override fun onItemClicked(skuDetails: SkuDetails?) {
        if (skuDetails == null) {
            screen.displayStoreConnectionError()
        } else {
            eventManager.sendClickBuyInAppEvent(skuDetails.sku)

            inAppManager.purchase(skuDetails, object : InAppManager.ActivityContainer {
                override fun get(): Activity {
                    return screen.getActivity()
                }
            })
        }
    }
}