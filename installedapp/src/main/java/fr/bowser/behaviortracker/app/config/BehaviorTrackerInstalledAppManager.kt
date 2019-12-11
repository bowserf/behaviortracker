package fr.bowser.behaviortracker.app.config

import fr.bowser.behaviortracker.app.instantapp.DaggerInstantAppComponentImpl
import fr.bowser.behaviortracker.app.widget.PomodoroAppWidgetProvider
import fr.bowser.behaviortracker.config.BehaviorTrackerApp
import fr.bowser.behaviortracker.instantapp.InstantAppComponent
import fr.bowser.behaviortracker.instantapp.InstantAppManagerProvider
import fr.bowser.behaviortracker.pomodoro.PomodoroManager
import fr.bowser.behaviortracker.timer.Timer

class BehaviorTrackerInstalledAppManager : BehaviorTrackerApp(), InstantAppManagerProvider {

    override fun onCreate() {
        super.onCreate()

        val providePomodoroManager = appComponent.providePomodoroManager()
        providePomodoroManager.addListener(createPomodoroListener())
    }

    private fun createPomodoroListener(): PomodoroManager.Listener {
        return object : PomodoroManager.Listener {
            override fun onPomodoroSessionStarted(newTimer: Timer, duration: Long) {
                PomodoroAppWidgetProvider.update(this@BehaviorTrackerInstalledAppManager)
            }

            override fun onPomodoroSessionStop() {
                PomodoroAppWidgetProvider.update(this@BehaviorTrackerInstalledAppManager)
            }

            override fun onTimerStateChanged(updatedTimer: Timer) {
                PomodoroAppWidgetProvider.update(this@BehaviorTrackerInstalledAppManager)
            }

            override fun updateTime(timer: Timer, currentTime: Long) {
                PomodoroAppWidgetProvider.update(this@BehaviorTrackerInstalledAppManager)
            }

            override fun onCountFinished(newTimer: Timer, duration: Long) {
                PomodoroAppWidgetProvider.update(this@BehaviorTrackerInstalledAppManager)
            }

        }
    }

    override fun provideMyInstantAppComponent(): InstantAppComponent {
        return DaggerInstantAppComponentImpl.builder().build()
    }
}