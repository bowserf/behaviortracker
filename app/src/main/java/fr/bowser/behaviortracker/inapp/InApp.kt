package fr.bowser.behaviortracker.inapp

import org.json.JSONException
import org.json.JSONObject

data class InApp(val sku: String, val name: String, val price: String) {

    fun toJson(): String {
        try {
            val jsonObject = JSONObject()
            jsonObject.put(JSON_SKU_KEY, sku)
            jsonObject.put(JSON_NAME_KEY, name)
            jsonObject.put(JSON_PRICE_KEY, price)
            return jsonObject.toString()
        } catch (e: JSONException) {
            throw IllegalStateException("Error when trying to convert InApp to JSON : " + e.message)
        }
    }

    companion object {
        private const val JSON_SKU_KEY = "sku"
        private const val JSON_NAME_KEY = "name"
        private const val JSON_PRICE_KEY = "price"

        fun fromJson(json: String): InApp {
            try {
                val jsonObject = JSONObject(json)
                val sku = jsonObject.getString(JSON_SKU_KEY)
                val name = jsonObject.getString(JSON_NAME_KEY)
                val price = jsonObject.getString(JSON_PRICE_KEY)
                return InApp(sku, name, price)
            } catch (e: JSONException) {
                throw IllegalStateException("Error when trying to convert JSON to" +
                        "InApp : " + e.message)
            }
        }
    }

}