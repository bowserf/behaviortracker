package fr.bowser.behaviortracker.timer_service

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
import android.content.res.Resources
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.core.content.ContextCompat
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.config.BehaviorTrackerApp
import fr.bowser.behaviortracker.home_activity.HomeActivity
import javax.inject.Inject

class TimerService : Service(), TimerServiceContract.Screen {

    @Inject
    lateinit var presenter: TimerServiceContract.Presenter

    private lateinit var notificationManager: NotificationManager

    private lateinit var pauseAction: NotificationCompat.Action

    private lateinit var resumeAction: NotificationCompat.Action

    private lateinit var continuePomodoroAction: NotificationCompat.Action

    private lateinit var interruptTimerAction: NotificationCompat.Action

    private var timerNotificationBuilder: NotificationCompat.Builder? = null

    override fun onCreate() {
        super.onCreate()

        notificationManager = baseContext.getSystemService(
            Context.NOTIFICATION_SERVICE,
        ) as NotificationManager

        setupGraph()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            generateNotificationChannel(baseContext.resources)
        }

        pauseAction = NotificationCompat.Action.Builder(
            R.drawable.timer_service_pause,
            baseContext.resources.getString(R.string.timer_notif_pause),
            TimerServiceReceiver.getPausePendingIntent(baseContext),
        ).build()

        resumeAction = NotificationCompat.Action.Builder(
            R.drawable.timer_service_play,
            baseContext.resources.getString(R.string.timer_notif_start),
            TimerServiceReceiver.getPlayPendingIntent(baseContext),
        ).build()

        interruptTimerAction = NotificationCompat.Action.Builder(
            R.drawable.timer_service_play,
            baseContext.resources.getString(R.string.timer_notif_interrupt_timer),
            TimerServiceReceiver.getInterruptTimerPendingIntent(baseContext),
        ).build()

        continuePomodoroAction = NotificationCompat.Action.Builder(
            R.drawable.notification_pomodoro,
            baseContext.resources.getString(R.string.timer_notif_continue_pomodoro),
            TimerServiceReceiver.getContinuePomodoroPendingIntent(baseContext),
        ).build()

        // action when click on notification
        val pendingHomeIntent = createHomeIntent()
        timerNotificationBuilder = NotificationCompat.Builder(
            baseContext,
            baseContext.resources.getString(R.string.timer_notif_channel_id),
        )
            .setSmallIcon(R.drawable.timer_service_timer)
            .setContentIntent(pendingHomeIntent)
            .setDeleteIntent(TimerServiceReceiver.getDeletePendingIntent(baseContext))

        presenter.attach()

        startForeground()
    }

    override fun onDestroy() {
        presenter.detach()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action
        if (action == ACTION_START) {
            startForeground()
            presenter.start()
        } else if (action == ACTION_DISMISS) {
            presenter.dismissNotification()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun renameTimerNotification(name: String) {
        timerNotificationBuilder!!.setContentTitle(name)
        updateNotificationContent()
    }

    override fun dismissNotification() {
        stopSelf()
        notificationManager.cancel(TIMER_NOTIFICATION_ID)
    }

    override fun updateTimeNotification(time: String) {
        timerNotificationBuilder!!.setContentText(time)
        updateNotificationContent()
    }

    override fun displayTimerNotification(title: String, message: String) {
        timerNotificationBuilder!!.let {
            it.setContentTitle(title)
            it.setContentText(message)
            it.clearActions()
            it.addAction(pauseAction)
            it.addAction(interruptTimerAction)
        }

        updateNotificationContent()
    }

    override fun resumeTimerNotification(title: String) {
        timerNotificationBuilder!!.let {
            it.setContentTitle(title)
            it.clearActions()
            it.addAction(pauseAction)
            it.addAction(interruptTimerAction)
        }

        updateNotificationContent()
    }

    override fun pauseTimerNotification() {
        timerNotificationBuilder!!.let {
            it.clearActions()
            it.addAction(resumeAction)
            it.addAction(interruptTimerAction)
        }

        updateNotificationContent()

        stopForeground()
    }

    override fun continuePomodoroNotification() {
        timerNotificationBuilder!!.let {
            it.clearActions()
            it.addAction(continuePomodoroAction)
            it.addAction(interruptTimerAction)
            it.setContentText(baseContext.getString(R.string.timer_notif_pomodoro_session_end))
        }

        updateNotificationContent()
    }

    private fun startForeground() {
        // Check if the app performs background starts
        // https://developer.android.com/guide/components/foreground-services#background-start-restrictions-check
        // Enable notification that appear each time the app attempts to launch a foreground service
        // while running in the background
        // adb shell device_config put activity_manager default_fgs_starts_restriction_notification_enabled true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(
                TIMER_NOTIFICATION_ID,
                timerNotificationBuilder!!.build(),
                FOREGROUND_SERVICE_TYPE_SPECIAL_USE,
            )
        } else {
            startForeground(
                TIMER_NOTIFICATION_ID,
                timerNotificationBuilder!!.build(),
            )
        }
    }

    private fun stopForeground() {
        ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_DETACH)
    }

    private fun updateNotificationContent() {
        notificationManager.notify(
            TIMER_NOTIFICATION_ID,
            timerNotificationBuilder!!.build(),
        )
    }

    private fun setupGraph() {
        val build = DaggerTimerServiceComponent.builder()
            .behaviorTrackerAppComponent(BehaviorTrackerApp.getAppComponent(baseContext!!))
            .timerServiceModule(TimerServiceModule(this))
            .build()
        build.inject(this)
    }

    private fun createHomeIntent(): PendingIntent {
        val homeIntent = Intent(baseContext, HomeActivity::class.java)
        homeIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
        return PendingIntent.getActivity(
            baseContext,
            0,
            homeIntent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            },
        )
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun generateNotificationChannel(res: Resources) {
        val channelId = res.getString(R.string.timer_notif_channel_id)
        val channelName = res.getString(R.string.timer_notif_channel_name)
        val channelDescription = res.getString(R.string.timer_notif_channel_description)
        val channelImportance = NotificationManager.IMPORTANCE_LOW
        val notificationChannel = NotificationChannel(
            channelId,
            channelName,
            channelImportance,
        )
        notificationChannel.description = channelDescription
        notificationChannel.enableVibration(false)
        notificationChannel.enableLights(false)
        notificationChannel.setShowBadge(false)
        notificationManager.createNotificationChannel(notificationChannel)
    }

    companion object {
        private const val TIMER_NOTIFICATION_ID = 42

        private const val ACTION_START = "time_service.action.start"
        private const val ACTION_DISMISS = "time_service.action.dismiss"

        fun start(context: Context) {
            val intent = Intent(context, TimerService::class.java)
            intent.action = ACTION_START
            ContextCompat.startForegroundService(context, intent)
        }

        fun dismissNotification(context: Context) {
            val intent = Intent(context, TimerService::class.java)
            intent.action = ACTION_DISMISS
            context.startService(intent)
        }
    }
}
