package fr.bowser.behaviortracker.timer

import androidx.test.platform.app.InstrumentationRegistry
import fr.bowser.behaviortracker.config.BehaviorTrackerApp
import kotlinx.coroutines.runBlocking

object TimerRepositoryClean {

    fun removeAllTimers() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val applicationContext = instrumentation.targetContext
        val appComponent = BehaviorTrackerApp.getAppComponent(applicationContext)
        val timerRepository = appComponent.provideTimerRepositoryManager()
        runBlocking {
            timerRepository.removeAllTimers().join()
        }
    }
}
