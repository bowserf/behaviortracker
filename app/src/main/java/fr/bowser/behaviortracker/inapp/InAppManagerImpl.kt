package fr.bowser.behaviortracker.inapp

import android.util.Log
import com.android.billingclient.api.*


class InAppManagerImpl(private val playBillingManager: PlayBillingManager,
                       private val inAppConfiguration: InAppConfiguration,
                       private val inAppRepository: InAppRepository) : InAppManager {

    private val ownedSku = mutableListOf<String>()

    private val listeners: MutableList<InAppManager.Listener> = mutableListOf()

    override fun initialize() {
        playBillingManager.setUpPlayBilling()
        playBillingManager.setPlayBillingManagerListener(createPlayBillingManagerListener())

        querySkuDetailsAsync()
    }

    override fun purchase(sku: String, activityContainer: InAppManager.ActivityContainer) {
        val purchaseFlowRequest = Runnable {
            val builder = BillingFlowParams
                    .newBuilder()
                    .setSku(sku)
                    .setType(BillingClient.SkuType.INAPP)
            val responseCode = playBillingManager.launchBillingFlow(
                    activityContainer.get(),
                    builder.build())
            if (responseCode == BillingClient.BillingResponse.ITEM_ALREADY_OWNED) {
                Log.e(TAG, "User already owns this in-app : $sku")
                notifyPurchaseFailed()
            } else if(responseCode != BillingClient.BillingResponse.OK){
                Log.e(TAG, "An error occurred during purchase, error code : $responseCode")
                notifyPurchaseFailed()
            }
        }
        playBillingManager.executeServiceRequest(purchaseFlowRequest)
    }

    private fun querySkuDetailsAsync() {
        val queryRequest = Runnable {
            val skuList = getListOfAvailableSku()
            val inAppSkuDetailsParams = SkuDetailsParams.newBuilder()
                    .setSkusList(skuList)
                    .setType(BillingClient.SkuType.INAPP)
                    .build()
            playBillingManager.querySkuDetailsAsync(inAppSkuDetailsParams,
                    createSkuDetailsResponseListener())
        }
        playBillingManager.executeServiceRequest(queryRequest)
    }

    private fun updatePurchasedInApp() {
        val queryBoughtInApp = Runnable {
            // legacy in-apps
            val purchases = playBillingManager.queryPurchases(BillingClient.SkuType.INAPP)
            val purchasesInAppList = purchases.purchasesList
            for (purchase in purchasesInAppList) {
                ownedSku.add(purchase.sku)
            }
        }
        playBillingManager.executeServiceRequest(queryBoughtInApp)
    }

    private fun getListOfAvailableSku(): List<String> {
        val inApps = inAppConfiguration.getInApps()
        val list = mutableListOf<String>()
        for (inApp in inApps) {
            list.add(inApp.sku)
        }
        return list
    }

    override fun addListener(listener: InAppManager.Listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener)
        }
    }

    override fun removeListener(listener: InAppManager.Listener) {
        listeners.remove(listener)
    }

    private fun notifyPurchaseFailed() {
        for (listener in listeners) {
            listener.onPurchaseFailed()
        }
    }

    private fun notifyPurchaseSucceed(purchases: List<Purchase>) {
        for (listener in listeners) {
            listener.onPurchaseSucceed(purchases)
        }
    }

    private fun createPlayBillingManagerListener(): PlayBillingManager.Listener {
        return object : PlayBillingManager.Listener {
            override fun onPurchasesUpdated(purchases: List<Purchase>?) {
                if (purchases != null) {
                    notifyPurchaseSucceed(purchases)
                }
            }

            override fun connectionToServiceFailed() {
                Log.e(TAG, "Connection to play store has failed.")
                notifyPurchaseFailed()
            }
        }
    }

    private fun createSkuDetailsResponseListener(): SkuDetailsResponseListener {
        return SkuDetailsResponseListener { responseCode, skuDetailsList ->
            if (responseCode == BillingClient.BillingResponse.OK) {
                inAppRepository.set(skuDetailsList)
                updatePurchasedInApp()
            }
        }
    }

    companion object {
        private const val TAG = "InAppManagerImpl"
    }

}