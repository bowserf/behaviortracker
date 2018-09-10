package fr.bowser.behaviortracker.rewards

interface InAppConfiguration {

    fun getInApp(sku: String): InApp

    fun parse(fileName: String): List<InApp>

}