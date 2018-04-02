package fr.bowser.behaviortracker.config

import android.app.Application
import android.content.Context
import android.os.StrictMode
import com.crashlytics.android.Crashlytics
import com.google.firebase.analytics.FirebaseAnalytics
import fr.bowser.behaviortracker.BuildConfig
import io.fabric.sdk.android.Fabric


class BehaviorTrackerApp : Application() {

    lateinit var appComponent: BehaviorTrackerAppComponent

    override fun onCreate() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build())
            StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build())
        }

        super.onCreate()

        setupGraph()

        setupCrashlytics()

        setupFirebaseAnalytics()
    }

    private fun setupCrashlytics() {
        Fabric.with(this, Crashlytics())
    }

    private fun setupGraph() {
        appComponent = DaggerBehaviorTrackerAppComponent.builder()
                .context(this)
                .build()
    }

    private fun setupFirebaseAnalytics() {
        FirebaseAnalytics.getInstance(this)
    }

    companion object {

        @JvmStatic
        fun getAppComponent(context: Context): BehaviorTrackerAppComponent {
            val app = context.applicationContext as BehaviorTrackerApp
            return app.appComponent
        }

    }

}