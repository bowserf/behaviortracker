package fr.bowser.behaviortracker.inapp

import androidx.test.runner.AndroidJUnit4
import com.android.billingclient.api.SkuDetails
import org.json.JSONObject
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class InAppRepositoryImplTest {

    @Test
    fun insertAndGet() {
        // Given
        val inAppConfig = InApp(
            "sku", "name", "description", "0.99 €",
            "feature"
        )
        val sharedPreference = HashMapSharedPreferenceConnected()
        val inAppRepositoryImpl = InAppRepositoryImpl(sharedPreference, listOf(inAppConfig))
        val list = mutableListOf<SkuDetails>()
        val jsonObject = JSONObject()
        jsonObject.put("productId", "sku")
        jsonObject.put("type", "inapp")
        jsonObject.put("price", "0.99 €")
        jsonObject.put("title", "name")
        jsonObject.put("description", "description")
        val skuDetails = SkuDetails(jsonObject.toString())
        list.add(skuDetails)
        inAppRepositoryImpl.set(list)

        // When
        val inApp = inAppRepositoryImpl.getInApp("sku")

        // Then
        Assert.assertEquals("sku", inApp.sku)
        Assert.assertEquals("name", inApp.name)
        Assert.assertEquals("description", inApp.description)
        Assert.assertEquals("0.99 €", inApp.price)
        Assert.assertEquals("feature", inApp.feature)
    }

    @Test
    fun restore() {
        // Given
        val sharedPreference = HashMapSharedPreferenceConnected()
        val set = mutableSetOf<String>()
        set.add(
            InApp(
                "sku1", "name1", "description1", "0.99 €",
                "feature1"
            ).toJson()
        )
        set.add(
            InApp(
                "sku2", "name2", "description2", "0.59 €",
                "feature2"
            ).toJson()
        )
        sharedPreference.edit().putStringSet(InAppRepositoryImpl.IN_APP_DETAILS_KEY, set)

        // When
        val inAppRepositoryImpl = InAppRepositoryImpl(sharedPreference, listOf())
        val inApp = inAppRepositoryImpl.getInApp("sku1")

        // Then
        Assert.assertEquals("sku1", inApp.sku)
        Assert.assertEquals("name1", inApp.name)
        Assert.assertEquals("description1", inApp.description)
        Assert.assertEquals("0.99 €", inApp.price)
        Assert.assertEquals("feature1", inApp.feature)
    }

    @Test
    fun fillWithConfiguration() {
        // Given
        val inAppConfig = InApp(
            "sku1", "name1", "description1",
            "0.99 €", "feature"
        )
        val sharedPreference = HashMapSharedPreferenceConnected()
        val inAppRepositoryImpl = InAppRepositoryImpl(sharedPreference, listOf(inAppConfig))

        // When
        val inApp = inAppRepositoryImpl.getInApp("sku1")

        // Then
        Assert.assertEquals("sku1", inApp.sku)
        Assert.assertEquals("name1", inApp.name)
        Assert.assertEquals("description1", inApp.description)
        Assert.assertEquals("0.99 €", inApp.price)
        Assert.assertEquals("feature", inApp.feature)
    }
}