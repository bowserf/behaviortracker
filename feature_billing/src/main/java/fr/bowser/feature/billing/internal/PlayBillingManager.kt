package fr.bowser.feature.billing.internal

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.AcknowledgePurchaseResponseListener
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetailsResponseListener
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesResponseListener
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams

internal class PlayBillingManager(context: Context) {

    private val context: Context = context.applicationContext

    private val purchasesUpdatedListener = createPurchaseUpdatedListener()

    private var billingClient: BillingClient? = null

    private var isServiceConnected = false

    private var listener: Listener? = null

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

    fun queryProductDetailsAsync(
        params: QueryProductDetailsParams,
        listener: ProductDetailsResponseListener,
    ) {
        checkBillingClientSetUp()
        billingClient!!.queryProductDetailsAsync(params, listener)
    }

    fun launchBillingFlow(activity: Activity, build: BillingFlowParams): BillingResult {
        checkBillingClientSetUp()
        return billingClient!!.launchBillingFlow(activity, build)
    }

    fun queryPurchasesAsync(sku: QueryPurchasesParams, listener: PurchasesResponseListener) {
        checkBillingClientSetUp()
        billingClient!!.queryPurchasesAsync(sku, listener)
    }

    fun acknowledgePurchase(
        acknowledgePurchaseParams: AcknowledgePurchaseParams,
        listener: AcknowledgePurchaseResponseListener,
    ) {
        checkBillingClientSetUp()
        billingClient!!.acknowledgePurchase(
            acknowledgePurchaseParams,
            listener,
        )
    }

    private fun startServiceConnection(executeOnSuccess: Runnable?) {
        checkBillingClientSetUp()
        billingClient!!.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(
                billingResult: BillingResult,
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
                "You should set up PlayBillingManager before calling this method.",
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
