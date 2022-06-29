package fr.bowser.behaviortracker.rewards_row_view

import android.app.Activity
import fr.bowser.behaviortracker.event.EventManager
import fr.bowser.feature.billing.InAppManager

class RewardsRowPresenter(
    private val screen: RewardsRowContract.Screen,
    private val inAppManager: InAppManager,
    private val eventManager: EventManager
) : RewardsRowContract.Presenter {

    override fun onItemClicked(sku: String) {
        eventManager.sendClickBuyInAppEvent(sku)
        inAppManager.purchase(
            sku,
            object : InAppManager.ActivityContainer {
                override fun get(): Activity {
                    return screen.getActivity()
                }
            }
        )
    }
}
