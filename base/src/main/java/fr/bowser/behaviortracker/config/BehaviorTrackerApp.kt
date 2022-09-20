package fr.bowser.behaviortracker.config

import android.app.Application
import android.content.Context
import android.os.StrictMode
import com.google.firebase.analytics.FirebaseAnalytics
import fr.bowser.behaviortracker.BuildConfig
import fr.bowser.behaviortracker.alarm.AlarmNotification
import fr.bowser.behaviortracker.alarm.AlarmStorageManagerModuleUA
import fr.bowser.behaviortracker.pomodoro.PomodoroManager
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.widget.PomodoroAppWidgetProvider
import fr.bowser.feature.alarm.AlarmGraph
import fr.bowser.feature.alarm.AlarmTimerManager
import fr.bowser.feature_do_not_disturb.DoNotDisturbGraph

class BehaviorTrackerApp : Application() {

    lateinit var appComponent: BehaviorTrackerAppComponent

    override fun onCreate() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build()
            )
            StrictMode.setVmPolicy(
                StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build()
            )
        }

        super.onCreate()

        setupGraph()

        setupFirebaseAnalytics()

        setupInAppManager()

        setupAlarmListener()
    }

    private fun setupInAppManager() {
        val inAppManager = appComponent.provideInAppManager()
        val inApps = appComponent.provideInAppConfiguration().getInApps()
        val skus = inApps.map { it.sku }
        inAppManager.initialize(skus)
    }

    private fun setupGraph() {
        appComponent = DaggerBehaviorTrackerAppComponent.builder()
            .context(this)
            .build()

        appInitialization()
    }

    private fun setupAlarmListener() {
        val alarmTimerManager = appComponent.provideAlarmTimerManager()
        alarmTimerManager.addListener(object : AlarmTimerManager.Listener {
            override fun onAlarmTriggered() {
                AlarmNotification.displayAlarmNotif(this@BehaviorTrackerApp)
            }
        })
    }

    private fun setupFirebaseAnalytics() {
        FirebaseAnalytics.getInstance(this)
    }

    private fun appInitialization() {
        val pomodoroManager = appComponent.providePomodoroManager()
        pomodoroManager.addListener(createPomodoroListener())
        AlarmGraph.init(this)
        DoNotDisturbGraph.init(this)
        if (BuildConfig.UA) {
            AlarmGraph.inject(AlarmStorageManagerModuleUA())
        }
    }

    private fun createPomodoroListener(): PomodoroManager.Listener {
        return object : PomodoroManager.Listener {
            override fun onPomodoroSessionStarted(newTimer: Timer, duration: Long) {
                PomodoroAppWidgetProvider.update(this@BehaviorTrackerApp)
            }

            override fun onPomodoroSessionStop() {
                PomodoroAppWidgetProvider.update(this@BehaviorTrackerApp)
            }

            override fun onTimerStateChanged(updatedTimer: Timer) {
                PomodoroAppWidgetProvider.update(this@BehaviorTrackerApp)
            }

            override fun updateTime(updatedTimer: Timer, currentTime: Long) {
                PomodoroAppWidgetProvider.update(this@BehaviorTrackerApp)
            }

            override fun onCountFinished(newTimer: Timer, duration: Long) {
                PomodoroAppWidgetProvider.update(this@BehaviorTrackerApp)
            }
        }
    }

    companion object {

        @JvmStatic
        fun getAppComponent(context: Context): BehaviorTrackerAppComponent {
            val app = context.applicationContext as BehaviorTrackerApp
            return app.appComponent
        }
    }
}
