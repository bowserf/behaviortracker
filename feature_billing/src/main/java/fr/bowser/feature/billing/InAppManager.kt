package fr.bowser.feature.billing

import android.app.Activity
import com.android.billingclient.api.Purchase

interface InAppManager {

    fun initialize(skus: List<String>)

    fun purchase(sku: String, activityContainer: ActivityContainer)

    fun getStorePurchases(): List<StorePurchase>

    fun addListener(listener: Listener)

    fun removeListener(listener: Listener)

    interface Listener {

        fun onPurchaseSucceed(purchases: List<StorePurchase>)

        fun onPurchaseFailed()
    }

    interface ActivityContainer {

        fun get(): Activity
    }

    data class StorePurchase(
        val sku: String,
        val productToken: String
    )
}
