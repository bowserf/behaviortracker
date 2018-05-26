package fr.bowser.behaviortracker.utils

import fr.bowser.behaviortracker.timer.Timer

class FakeTimer {

    companion object {
        val timer = Timer(-1, 0, "", -1, -1)
    }

}