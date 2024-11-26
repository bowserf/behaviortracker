package fr.bowser.feature.toast

interface ToastManager {

    fun showText(text: String)

    fun showText(textRes: Int)
}
