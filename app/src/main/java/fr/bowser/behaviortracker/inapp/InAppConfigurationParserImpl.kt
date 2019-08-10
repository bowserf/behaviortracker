package fr.bowser.behaviortracker.inapp

import android.content.res.AssetManager

class InAppConfigurationParserImpl(private val assetManager: AssetManager) :
    InAppConfigurationParser {

    override fun getInAppConfigFile(fileName: String): String {
        val inputStream = assetManager.open(fileName)
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        return String(buffer)
    }
}