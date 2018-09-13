package fr.bowser.behaviortracker.inapp

import android.app.Activity
import com.android.billingclient.api.Purchase

interface InAppManager {

    fun initialize()

    fun purchase(sku: String, activityContainer: InAppManager.ActivityContainer)

    fun addListener(listener: Listener)

    fun removeListener(listener: Listener)

    interface Listener {

        fun onPurchaseSucceed(purchases: List<Purchase>)

        fun onPurchaseFailed()

    }

    interface ActivityContainer{

        fun get(): Activity

    }

}