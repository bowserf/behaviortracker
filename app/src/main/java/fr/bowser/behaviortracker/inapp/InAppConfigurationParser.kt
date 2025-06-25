package fr.bowser.behaviortracker.inapp

interface InAppConfigurationParser {

    fun getInAppConfigFile(fileName: String): String
}
