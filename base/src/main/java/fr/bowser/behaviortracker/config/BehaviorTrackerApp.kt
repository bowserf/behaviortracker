package fr.bowser.behaviortracker.config

import android.app.Application
import android.content.Context
import android.os.StrictMode
import com.crashlytics.android.Crashlytics
import com.google.android.gms.common.wrappers.InstantApps
import com.google.firebase.analytics.FirebaseAnalytics
import fr.bowser.behaviortracker.BuildConfig
import fr.bowser.behaviortracker.instantapp.InstantAppManagerProviderHelper
import io.fabric.sdk.android.Fabric

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

        setupCrashlytics()

        setupFirebaseAnalytics()

        setupInAppManager()
    }

    private fun setupInAppManager() {
        val inAppManager = appComponent.provideInAppManager()
        inAppManager.initialize()
    }

    private fun setupCrashlytics() {
        Fabric.with(this, Crashlytics())
        Crashlytics.setBool(
            CRASHLYTICS_IS_INSTANT_APP,
            appComponent.provideMyInstantApp().isInstantApp()
        )
    }

    private fun setupGraph() {
        val isInstantApp = InstantApps.isInstantApp(this)
        appComponent = DaggerBehaviorTrackerAppComponent.builder()
            .myInstantApp(InstantAppManagerProviderHelper.provideMyInstantAppComponent(isInstantApp))
            .context(this)
            .build()
    }

    private fun setupFirebaseAnalytics() {
        FirebaseAnalytics.getInstance(this)
    }

    companion object {

        private const val CRASHLYTICS_IS_INSTANT_APP = "is_instant_app"

        @JvmStatic
        fun getAppComponent(context: Context): BehaviorTrackerAppComponent {
            val app = context.applicationContext as BehaviorTrackerApp
            return app.appComponent
        }
    }
}