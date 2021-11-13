package fr.bowser.behaviortracker.config

import android.app.Application
import android.content.Context
import android.os.StrictMode
import com.google.android.gms.common.wrappers.InstantApps
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import fr.bowser.behaviortracker.BuildConfig
import fr.bowser.behaviortracker.alarm.AlarmStorageManagerModuleUA
import fr.bowser.behaviortracker.app_initialization.AppInitializationProviderHelper
import fr.bowser.behaviortracker.instantapp.InstantAppManagerProviderHelper
import fr.bowser.feature.alarm.AlarmGraph
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

        setupCrashlytics()

        setupFirebaseAnalytics()

        setupInAppManager()
    }

    private fun setupInAppManager() {
        val inAppManager = appComponent.provideInAppManager()
        inAppManager.initialize()
    }

    private fun setupCrashlytics() {
        FirebaseCrashlytics.getInstance().setCustomKey(
            CRASHLYTICS_IS_INSTANT_APP,
            appComponent.provideMyInstantApp().isInstantApp()
        )
    }

    private fun setupGraph() {
        val isInstantApp = InstantApps.isInstantApp(this)
        appComponent = DaggerBehaviorTrackerAppComponent.builder()
            .myInstantApp(InstantAppManagerProviderHelper.provideMyInstantAppComponent(isInstantApp))
            .appInitialization(
                AppInitializationProviderHelper.provideAppInitializationComponent(
                    this,
                    isInstantApp
                )
            )
            .context(this)
            .build()

        AlarmGraph.init(this)
        DoNotDisturbGraph.init(this)
        appComponent.provideAppInitialization().initialize()
        if (BuildConfig.UA) {
            AlarmGraph.inject(AlarmStorageManagerModuleUA())
        }
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
