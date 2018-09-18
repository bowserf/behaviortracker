package fr.bowser.behaviortracker.inapp

import android.content.SharedPreferences
import androidx.annotation.VisibleForTesting
import com.android.billingclient.api.SkuDetails
import java.util.*

class InAppRepositoryImpl(private val sharedPreferences: SharedPreferences,
                          private val inAppsConfig: List<InApp>) : InAppRepository {

    private val detailsList: MutableList<InApp> = mutableListOf()

    init {
        restoreSubscriptionsDetailsFromStorage()
    }

    override fun getInApps(): List<InApp> {
        return detailsList
    }

    override fun getInApp(sku: String): InApp {
        for (inApp in detailsList) {
            if (inApp.sku == sku) {
                return inApp
            }
        }
        throw IllegalStateException("Unknown SKU : $sku")
    }

    override fun set(skuDetails: List<SkuDetails>) {
        val list = ArrayList<InApp>()
        for (skuDetail in skuDetails) {
            val sku = skuDetail.sku
            val name = skuDetail.title
            val description = skuDetail.description
            val priceAndCurrency = skuDetail.price
            val feature = getFeatureFromSku(sku)
            list.add(InApp(sku, name, description, priceAndCurrency, feature))
        }

        detailsList.clear()
        detailsList.addAll(list)

        saveSubscriptionDetailsListOnDisk()
    }

    private fun getFeatureFromSku(sku: String): String {
        for (inApp in inAppsConfig) {
            if (inApp.sku == sku) {
                return inApp.feature
            }
        }
        throw IllegalStateException("Unknown sku : $sku")
    }

    private fun saveSubscriptionDetailsListOnDisk() {
        val edit = sharedPreferences.edit()
        val json = HashSet<String>()
        for (inApp in detailsList) {
            json.add(inApp.toJson())
        }
        edit.putStringSet(IN_APP_DETAILS_KEY, json)
        edit.apply()
    }

    private fun restoreSubscriptionsDetailsFromStorage() {
        detailsList.clear()
        val inAppSet = sharedPreferences.getStringSet(IN_APP_DETAILS_KEY, null)

        if (inAppSet == null) {
            detailsList.addAll(inAppsConfig)
        } else {
            for (inAppJson in inAppSet) {
                val inApp = InApp.fromJson(inAppJson)
                detailsList.add(inApp)
            }
        }
    }

    companion object {
        const val SHARED_PREF_KEY = "in-app-details-storage"
        @VisibleForTesting
        const val IN_APP_DETAILS_KEY = "in_app_details_repository.key.details"
    }

}