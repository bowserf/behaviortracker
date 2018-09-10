package fr.bowser.behaviortracker.rewards

interface InAppConfigurationParser {

    fun getInAppConfigFile(fileName: String): String

}