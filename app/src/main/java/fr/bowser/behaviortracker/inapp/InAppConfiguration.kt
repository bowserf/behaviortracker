package fr.bowser.behaviortracker.inapp

interface InAppConfiguration {

    fun getInApp(sku: String): InApp

    fun getInApps(): List<InApp>

    fun parse(fileName: String): List<InApp>

}