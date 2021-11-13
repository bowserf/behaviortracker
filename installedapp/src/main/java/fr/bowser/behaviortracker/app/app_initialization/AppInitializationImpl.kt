package fr.bowser.behaviortracker.app.app_initialization

import android.content.Context
import fr.bowser.behaviortracker.app.widget.PomodoroAppWidgetProvider
import fr.bowser.behaviortracker.app_initialization.AppInitialization
import fr.bowser.behaviortracker.config.BehaviorTrackerApp
import fr.bowser.behaviortracker.pomodoro.PomodoroManager
import fr.bowser.behaviortracker.timer.Timer

class AppInitializationImpl(
    private val context: Context
) : AppInitialization {

    override fun initialize() {
        val pomodoroManager =
            (context.applicationContext as BehaviorTrackerApp).appComponent.providePomodoroManager()
        pomodoroManager.addListener(createPomodoroListener())
    }

    private fun createPomodoroListener(): PomodoroManager.Listener {
        return object : PomodoroManager.Listener {
            override fun onPomodoroSessionStarted(newTimer: Timer, duration: Long) {
                PomodoroAppWidgetProvider.update(context)
            }

            override fun onPomodoroSessionStop() {
                PomodoroAppWidgetProvider.update(context)
            }

            override fun onTimerStateChanged(updatedTimer: Timer) {
                PomodoroAppWidgetProvider.update(context)
            }

            override fun updateTime(timer: Timer, currentTime: Long) {
                PomodoroAppWidgetProvider.update(context)
            }

            override fun onCountFinished(newTimer: Timer, duration: Long) {
                PomodoroAppWidgetProvider.update(context)
            }
        }
    }
}
