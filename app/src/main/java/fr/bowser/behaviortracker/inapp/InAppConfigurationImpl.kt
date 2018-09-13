package fr.bowser.behaviortracker.inapp

import org.json.JSONObject

class InAppConfigurationImpl(private val inAppConfigurationParser: InAppConfigurationParser) : InAppConfiguration {

    private val inAppList = mutableListOf<InApp>()

    init {
        parse(IN_APP_CONFIG_FILE_PATH + IN_APP_CONFIG_FILE_NAME)
    }

    override fun getInApp(sku: String): InApp {
        for (inApp in inAppList) {
            if(inApp.sku == sku){
                return inApp
            }
        }
        throw IllegalStateException("Unknown product ID $sku.")
    }

    override fun getInApps(): List<InApp> {
        return inAppList.toList()
    }

    override fun parse(fileName: String): List<InApp> {
        val inAppConfigFile = inAppConfigurationParser.getInAppConfigFile(fileName)
        inAppList.clear()
        inAppList.addAll(parseJson(inAppConfigFile))
        return inAppList
    }

    private fun parseJson(json: String): List<InApp> {
        val list = mutableListOf<InApp>()

        val jsonObject = JSONObject(json)
        val jsonArray = jsonObject.getJSONArray(IN_APPS_KEY)
        val length = jsonArray.length()
        for (i in 0 until length) {
            val inAppObject = jsonArray.getJSONObject(i)
            val sku = inAppObject.getString(SKU_KEY)
            val name = inAppObject.getString(DEFAULT_NAME_KEY)
            val defaultPrice = inAppObject.getString(DEFAULT_PRICE_KEY)
            list.add(InApp(sku, name, defaultPrice))
        }

        return list
    }

    companion object {
        private const val IN_APP_CONFIG_FILE_PATH = "rewards/"
        private const val IN_APP_CONFIG_FILE_NAME = "in-app-config.json"

        private const val IN_APPS_KEY = "in-apps"
        private const val SKU_KEY = "identifier"
        private const val DEFAULT_PRICE_KEY = "default_price"
        private const val DEFAULT_NAME_KEY = "default_name"
    }

}