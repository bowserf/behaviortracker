package fr.bowser.behaviortracker.inapp

import fr.bowser.behaviortracker.inapp.InAppConfigurationImpl
import fr.bowser.behaviortracker.inapp.InAppConfigurationParser
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class InAppConfigurationImplTest {


    @Mock
    private lateinit var inAppConfigurationParser: InAppConfigurationParser

    @Test
    fun parse(){
        // Giving
        val inAppConfigurationImpl = InAppConfigurationImpl(inAppConfigurationParser)
        val fakeJson = fakeJson()
        Mockito.`when`(inAppConfigurationParser.getInAppConfigFile(Mockito.anyString())).thenReturn(fakeJson)

        // When
        val output = inAppConfigurationImpl.parse(fakeJson)

        // Then
        Assert.assertEquals(2, output.size)
        Assert.assertEquals("Marshmallow", output[0].name)
        Assert.assertEquals("0.59 €", output[0].price)
        Assert.assertEquals("fr.bowser.behaviortrack.product.marshmallow", output[0].sku)
        Assert.assertEquals("Nougat", output[1].name)
        Assert.assertEquals("0.99 €", output[1].price)
        Assert.assertEquals("fr.bowser.behaviortrack.product.nougat", output[1].sku)
    }

    @Test
    fun getInApp(){
        // Giving
        val inAppConfigurationImpl = InAppConfigurationImpl(inAppConfigurationParser)
        val fakeJson = fakeJson()
        Mockito.`when`(inAppConfigurationParser.getInAppConfigFile(Mockito.anyString())).thenReturn(fakeJson)
        inAppConfigurationImpl.parse(Mockito.anyString())

        // When
        val inApp = inAppConfigurationImpl.getInApp("fr.bowser.behaviortrack.product.marshmallow")

        // Then
        Assert.assertEquals("fr.bowser.behaviortrack.product.marshmallow", inApp.sku)
        Assert.assertEquals("Marshmallow", inApp.name)
        Assert.assertEquals("0.59 €", inApp.price)
    }

    private fun fakeJson(): String{
        return "{\n" +
                "  \"version\": \"1.0\",\n" +
                "  \"in-apps\": [\n" +
                "    {\n" +
                "      \"identifier\": \"fr.bowser.behaviortrack.product.marshmallow\",\n" +
                "      \"default_name\": \"Marshmallow\",\n" +
                "      \"default_price\": 0.59 €\n" +
                "    },\n" +
                "    {\n" +
                "      \"identifier\": \"fr.bowser.behaviortrack.product.nougat\",\n" +
                "      \"default_name\": \"Nougat\",\n" +
                "      \"default_price\": 0.99 €\n" +
                "    }\n" +
                "  ]\n" +
                "}"
    }

}