package fr.bowser.behaviortracker.screenshot

import androidx.test.core.graphics.writeToTestStorage

object Screenshot {

    fun takeScreenshot(name: String) {
        androidx.test.core.app.takeScreenshot().writeToTestStorage(name)
    }
}
