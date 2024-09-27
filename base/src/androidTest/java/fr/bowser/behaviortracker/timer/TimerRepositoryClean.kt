package fr.bowser.behaviortracker.timer

import androidx.test.platform.app.InstrumentationRegistry
import fr.bowser.behaviortracker.config.BehaviorTrackerApp

object TimerRepositoryClean {

    fun removeAllTimers() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val applicationContext = instrumentation.targetContext
        val appComponent = BehaviorTrackerApp.getAppComponent(applicationContext)
        val timerRepository = appComponent.provideTimerRepositoryManager()
        instrumentation.runOnMainSync {
            timerRepository.removeAllTimers()
        }
    }
}
