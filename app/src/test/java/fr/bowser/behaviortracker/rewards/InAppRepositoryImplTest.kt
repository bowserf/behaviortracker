package fr.bowser.behaviortracker.rewards

import com.android.billingclient.api.SkuDetails
import org.json.JSONObject
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class InAppRepositoryImplTest {

    @Mock
    private lateinit var inAppConfiguration: InAppConfiguration

    @Test
    fun insertAndGet() {
        // Given
        val sharedPreference = HashMapSharedPreference()
        val inAppRepositoryImpl = InAppRepositoryImpl(sharedPreference, inAppConfiguration)
        val list = mutableListOf<SkuDetails>()
        val jsonObject = JSONObject()
        jsonObject.put("productId", "sku")
        jsonObject.put("type", "inapp")
        jsonObject.put("price", "0.99 €")
        jsonObject.put("title", "name")
        val skuDetails = SkuDetails(jsonObject.toString())
        list.add(skuDetails)
        inAppRepositoryImpl.set(list)

        // When
        val inApp = inAppRepositoryImpl.getInApp("sku")

        // Then
        Assert.assertEquals("sku", inApp.sku)
        Assert.assertEquals("name", inApp.name)
        Assert.assertEquals("0.99 €", inApp.price)
    }

    @Test
    fun restore() {
        // Given
        val sharedPreference = HashMapSharedPreference()
        val set = mutableSetOf<String>()
        set.add(InApp("sku1", "name1", "0.99 €").toJson())
        set.add(InApp("sku2", "name2", "0.59 €").toJson())
        sharedPreference.edit().putStringSet(InAppRepositoryImpl.IN_APP_DETAILS_KEY, set)

        // When
        val inAppRepositoryImpl = InAppRepositoryImpl(sharedPreference, inAppConfiguration)
        val inApp = inAppRepositoryImpl.getInApp("sku1")

        // Then
        Assert.assertEquals("sku1", inApp.sku)
        Assert.assertEquals("name1", inApp.name)
        Assert.assertEquals("0.99 €", inApp.price)
    }

    @Test
    fun fillWithConfiguration() {
        // Given
        val inAppConfig = InApp("sku1", "name1", "0.99 €")
        Mockito.`when`(inAppConfiguration.getInApp("sku1")).thenReturn(inAppConfig)
        val sharedPreference = HashMapSharedPreference()
        val inAppRepositoryImpl = InAppRepositoryImpl(sharedPreference, inAppConfiguration)

        // When
        val inApp = inAppRepositoryImpl.getInApp("sku1")

        // Then
        Assert.assertEquals("sku1", inApp.sku)
        Assert.assertEquals("name1", inApp.name)
        Assert.assertEquals("0.99 €", inApp.price)
    }

}