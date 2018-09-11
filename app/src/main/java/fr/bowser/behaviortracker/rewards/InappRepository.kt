package fr.bowser.behaviortracker.rewards

import com.android.billingclient.api.SkuDetails

interface InappRepository {

    fun getInApp(sku: String): InApp

    fun set(skuDetails: List<SkuDetails>)

}