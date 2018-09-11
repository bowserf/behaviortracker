package fr.bowser.behaviortracker.inapp

import com.android.billingclient.api.SkuDetails

interface InAppRepository {

    fun getInApp(sku: String): InApp

    fun set(skuDetails: List<SkuDetails>)

}