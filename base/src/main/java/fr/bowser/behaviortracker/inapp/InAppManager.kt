package fr.bowser.behaviortracker.inapp

import android.app.Activity
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.SkuDetails

interface InAppManager {

    fun initialize()

    fun purchase(skuDetails: SkuDetails, activityContainer: ActivityContainer)

    fun addListener(listener: Listener)

    fun removeListener(listener: Listener)

    interface Listener {

        fun onPurchaseSucceed(purchases: List<Purchase>)

        fun onPurchaseFailed()
    }

    interface ActivityContainer {

        fun get(): Activity
    }
}