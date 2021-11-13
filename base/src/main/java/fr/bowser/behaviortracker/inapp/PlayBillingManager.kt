package fr.bowser.behaviortracker.inapp

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.AcknowledgePurchaseResponseListener
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.SkuDetailsParams
import com.android.billingclient.api.SkuDetailsResponseListener

class PlayBillingManager(context: Context) {

    private val context: Context = context.applicationContext

    private val purchasesUpdatedListener: PurchasesUpdatedListener

    private var billingClient: BillingClient? = null

    private var isServiceConnected = false

    private var listener: Listener? = null

    init {
        purchasesUpdatedListener = createPurchaseUpdatedListener()
    }

    fun setUpPlayBilling() {
        billingClient = BillingClient.newBuilder(context)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases()
            .build()
    }

    fun release() {
        billingClient?.let {
            if (it.isReady) {
                it.endConnection()
            }
        }
    }

    fun executeServiceRequest(runnable: Runnable) {
        if (isServiceConnected) {
            runnable.run()
        } else {
            startServiceConnection(runnable)
        }
    }

    fun setPlayBillingManagerListener(listener: Listener) {
        this.listener = listener
    }

    fun querySkuDetailsAsync(
        build: SkuDetailsParams,
        skuDetailsResponseListener: SkuDetailsResponseListener
    ) {
        checkBillingClientSetUp()
        billingClient!!.querySkuDetailsAsync(build, skuDetailsResponseListener)
    }

    fun launchBillingFlow(activity: Activity, build: BillingFlowParams): BillingResult {
        checkBillingClientSetUp()
        return billingClient!!.launchBillingFlow(activity, build)
    }

    fun queryPurchases(@BillingClient.SkuType sku: String): Purchase.PurchasesResult {
        checkBillingClientSetUp()
        return billingClient!!.queryPurchases(sku)
    }

    fun acknowledgePurchase(
        acknowledgePurchaseParams: AcknowledgePurchaseParams?,
        acknowledgePurchaseResponseListener: AcknowledgePurchaseResponseListener?
    ) {
        checkBillingClientSetUp()
        billingClient!!.acknowledgePurchase(
            acknowledgePurchaseParams!!,
            acknowledgePurchaseResponseListener!!
        )
    }

    private fun startServiceConnection(executeOnSuccess: Runnable?) {
        checkBillingClientSetUp()
        billingClient!!.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(
                billingResult: BillingResult
            ) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    isServiceConnected = true
                    executeOnSuccess?.run()
                } else {
                    listener?.connectionToServiceFailed()
                }
            }

            override fun onBillingServiceDisconnected() {
                isServiceConnected = false
            }
        })
    }

    private fun checkBillingClientSetUp() {
        if (billingClient == null) {
            throw IllegalStateException(
                "You should set up PlayBillingManager before calling this method."
            )
        }
    }

    private fun createPurchaseUpdatedListener(): PurchasesUpdatedListener {
        return PurchasesUpdatedListener { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                listener?.onPurchasesUpdated(purchases)
            }
        }
    }

    interface Listener {
        fun onPurchasesUpdated(purchases: List<Purchase>?)

        fun connectionToServiceFailed()
    }
}
