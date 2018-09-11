package fr.bowser.behaviortracker.rewards

interface InAppConfiguration {

    fun getInApp(sku: String): InApp

    fun getInApps(): List<InApp>

    fun parse(fileName: String): List<InApp>

}