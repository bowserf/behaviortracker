package fr.bowser.behaviortracker.inapp

import org.json.JSONException
import org.json.JSONObject

data class InApp(
    val sku: String,
    val name: String,
    val description: String,
    val price: String,
    val feature: String
) {

    fun toJson(): String {
        try {
            val jsonObject = JSONObject()
            jsonObject.put(JSON_SKU_KEY, sku)
            jsonObject.put(JSON_NAME_KEY, name)
            jsonObject.put(JSON_DESCRIPTION, description)
            jsonObject.put(JSON_PRICE_KEY, price)
            jsonObject.put(JSON_FEATURE, feature)
            return jsonObject.toString()
        } catch (e: JSONException) {
            throw IllegalStateException("Error when trying to convert InApp to JSON : " + e.message)
        }
    }

    companion object {

        const val FEATURE_MARSHMALLOW = "marshmallow"
        const val FEATURE_NOUGAT = "nougat"
        const val FEATURE_OREO = "oreo"
        const val FEATURE_PIE = "pie"

        private const val JSON_SKU_KEY = "sku"
        private const val JSON_NAME_KEY = "name"
        private const val JSON_DESCRIPTION = "description"
        private const val JSON_PRICE_KEY = "price"
        private const val JSON_FEATURE = "feature"

        fun fromJson(json: String): InApp {
            try {
                val jsonObject = JSONObject(json)
                val sku = jsonObject.getString(JSON_SKU_KEY)
                val name = jsonObject.getString(JSON_NAME_KEY)
                val description = jsonObject.getString(JSON_DESCRIPTION)
                val price = jsonObject.getString(JSON_PRICE_KEY)
                val feature = jsonObject.getString(JSON_FEATURE)
                return InApp(sku, name, description, price, feature)
            } catch (e: JSONException) {
                throw IllegalStateException(
                    "Error when trying to convert JSON to" +
                            "InApp : " + e.message
                )
            }
        }
    }
}