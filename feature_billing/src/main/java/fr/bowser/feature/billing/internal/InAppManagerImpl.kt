package fr.bowser.feature.billing.internal

import android.util.Log
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.AcknowledgePurchaseResponseListener
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.ProductDetailsResponseListener
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesResponseListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import fr.bowser.feature.billing.InAppManager

internal class InAppManagerImpl(
    private val playBillingManager: PlayBillingManager,
) : InAppManager {

    private val storePurchases = mutableListOf<InAppManager.StorePurchase>()

    private val productDetails = mutableListOf<ProductDetails>()

    private val listeners: MutableList<InAppManager.Listener> = mutableListOf()

    override fun initialize(skus: List<String>) {
        playBillingManager.setUpPlayBilling()
        playBillingManager.setPlayBillingManagerListener(createPlayBillingManagerListener())

        queryProductDetails(skus)
    }

    override fun purchase(sku: String, activityContainer: InAppManager.ActivityContainer) {
        val productDetail = productDetails.firstOrNull { it.productId == sku } ?: return
        val purchaseFlowRequest = Runnable {
            val productDetailsParamsList = listOf(
                BillingFlowParams.ProductDetailsParams.newBuilder()
                    .setProductDetails(productDetail)
                    .build(),
            )
            val builder = BillingFlowParams
                .newBuilder()
                .setProductDetailsParamsList(productDetailsParamsList)
            val billingResult = playBillingManager.launchBillingFlow(
                activityContainer.get(),
                builder.build(),
            )
            if (billingResult.responseCode == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
                Log.e(TAG, "User already owns this in-app : $sku")
                notifyPurchaseFailed()
            } else if (billingResult.responseCode != BillingClient.BillingResponseCode.OK) {
                Log.e(
                    TAG,
                    "An error occurred during purchase, error code : ${billingResult.responseCode}",
                )
                notifyPurchaseFailed()
            }
        }
        playBillingManager.executeServiceRequest(purchaseFlowRequest)
    }

    override fun getStorePurchases(): List<InAppManager.StorePurchase> {
        return storePurchases
    }

    private fun queryProductDetails(skuList: List<String>) {
        val queryRequest = Runnable {
            val productList = skuList.map {
                QueryProductDetailsParams.Product.newBuilder()
                    .setProductId(it)
                    .setProductType(BillingClient.ProductType.INAPP)
                    .build()
            }
            val params = QueryProductDetailsParams.newBuilder().setProductList(productList).build()
            playBillingManager.queryProductDetailsAsync(
                params,
                createProductDetailsResponseListener(),
            )
        }
        playBillingManager.executeServiceRequest(queryRequest)
    }

    private fun queryPurchases() {
        val queryBoughtInApp = Runnable {
            val listener = PurchasesResponseListener { _, purchases ->
                val storePurchases = convertPurchaseToStorePurchase(purchases)
                this@InAppManagerImpl.storePurchases.addAll(storePurchases)
            }
            val queryPurchasesParams = QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
            playBillingManager.queryPurchasesAsync(queryPurchasesParams, listener)
        }
        playBillingManager.executeServiceRequest(queryBoughtInApp)
    }

    private fun acknowledgeIfNeeded(purchases: List<Purchase>?) {
        if (purchases == null) {
            return
        }
        for (purchase in purchases) {
            acknowledgeIfNeeded(purchase)
        }
    }

    private fun acknowledgeIfNeeded(purchase: Purchase) {
        if (purchase.purchaseState != Purchase.PurchaseState.PURCHASED) {
            return
        }
        if (purchase.isAcknowledged) {
            return
        }
        val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()
        val acknowledgePurchaseResponseListener =
            AcknowledgePurchaseResponseListener { billingResult ->
                val responseCode = billingResult.responseCode
                if (responseCode != BillingClient.BillingResponseCode.OK) {
                    notifyPurchaseFailed()
                }
            }
        playBillingManager.acknowledgePurchase(
            acknowledgePurchaseParams,
            acknowledgePurchaseResponseListener,
        )
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

    private fun notifyPurchaseSucceed(purchases: List<InAppManager.StorePurchase>) {
        for (listener in listeners) {
            listener.onPurchaseSucceed(purchases)
        }
    }

    private fun createPlayBillingManagerListener(): PlayBillingManager.Listener {
        return object : PlayBillingManager.Listener {
            override fun onPurchasesUpdated(purchases: List<Purchase>?) {
                acknowledgeIfNeeded(purchases)
                if (purchases != null) {
                    val storePurchases = convertPurchaseToStorePurchase(purchases)
                    notifyPurchaseSucceed(storePurchases)
                }
            }

            override fun connectionToServiceFailed() {
                Log.e(TAG, "Connection to play store has failed.")
                notifyPurchaseFailed()
            }
        }
    }

    private fun convertPurchaseToStorePurchase(purchases: List<Purchase>): List<InAppManager.StorePurchase> {
        return purchases.map { InAppManager.StorePurchase(it.products.first(), it.purchaseToken) }
    }

    private fun createProductDetailsResponseListener(): ProductDetailsResponseListener {
        return ProductDetailsResponseListener { result, productDetails ->
            if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                this@InAppManagerImpl.productDetails.clear()
                this@InAppManagerImpl.productDetails.addAll(productDetails)
                queryPurchases()
            }
        }
    }

    companion object {
        private const val TAG = "InAppManagerImpl"
    }
}
