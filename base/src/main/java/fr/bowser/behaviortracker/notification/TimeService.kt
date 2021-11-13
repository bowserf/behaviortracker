package fr.bowser.behaviortracker.notification

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.config.BehaviorTrackerApp
import fr.bowser.behaviortracker.home_view.HomeActivity
import javax.inject.Inject

class TimeService : Service(), TimeContract.Screen {

    @Inject
    lateinit var presenter: TimeContract.Presenter

    private lateinit var notificationManager: NotificationManager

    private lateinit var pauseAction: NotificationCompat.Action

    private lateinit var resumeAction: NotificationCompat.Action

    private lateinit var continuePomodoroAction: NotificationCompat.Action

    private var timerNotificationBuilder: NotificationCompat.Builder? = null

    override fun onCreate() {
        super.onCreate()

        notificationManager = baseContext.getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager

        setupGraph()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            generateNotificationChannel(baseContext.resources)
        }

        pauseAction = NotificationCompat.Action.Builder(
            R.drawable.ic_pause,
            baseContext.resources.getString(R.string.timer_notif_pause),
            TimerReceiver.getPausePendingIntent(baseContext)
        ).build()

        resumeAction = NotificationCompat.Action.Builder(
            R.drawable.ic_play,
            baseContext.resources.getString(R.string.timer_notif_start),
            TimerReceiver.getPlayPendingIntent(baseContext)
        ).build()

        continuePomodoroAction = NotificationCompat.Action.Builder(
            R.drawable.notification_pomodoro,
            baseContext.resources.getString(R.string.timer_notif_continue_pomodoro),
            TimerReceiver.getContinuePomodoroPendingIntent(baseContext)
        ).build()

        // action when click on notification
        val pendingHomeIntent = createHomeIntent()
        timerNotificationBuilder = NotificationCompat.Builder(
            baseContext,
            baseContext.resources.getString(R.string.timer_notif_channel_id)
        )
            .setSmallIcon(R.drawable.ic_timer)
            .setContentIntent(pendingHomeIntent)
            .setDeleteIntent(TimerReceiver.getDeletePendingIntent(baseContext))

        presenter.attach()

        updateNotification()
    }

    override fun onDestroy() {
        presenter.detach()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val action = intent.action
        if (action == ACTION_START) {
            updateNotification()
            presenter.start()
        } else if (action == ACTION_DISMISS) {
            presenter.dismissNotification()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun renameTimerNotification(name: String) {
        timerNotificationBuilder!!.setContentTitle(name)
        updateNotification()
    }

    override fun dismissNotification() {
        stopForeground(true)
        stopSelf()
    }

    override fun updateTimeNotification(time: String) {
        timerNotificationBuilder!!.setContentText(time)
        updateNotification()
    }

    override fun displayTimerNotification(title: String, message: String) {
        timerNotificationBuilder!!.let {
            it.setContentTitle(title)
            it.setContentText(message)
            it.mActions?.clear()
            it.addAction(pauseAction)
        }

        updateNotification()
    }

    override fun resumeTimerNotification(title: String) {
        timerNotificationBuilder!!.let {
            it.setContentTitle(title)
            it.mActions?.clear()
            it.addAction(pauseAction)
        }

        updateNotification()
    }

    override fun pauseTimerNotification() {
        timerNotificationBuilder!!.let {
            it.mActions?.clear()
            it.addAction(resumeAction)
        }

        notificationManager.notify(
            TIMER_NOTIFICATION_ID,
            timerNotificationBuilder!!.build()
        )

        stopForeground(false)
    }

    override fun continuePomodoroNotification() {
        timerNotificationBuilder!!.let {
            it.mActions?.clear()
            it.addAction(continuePomodoroAction)
            it.setContentText(baseContext.getString(R.string.timer_notif_pomodoro_session_end))
        }

        updateNotification()
    }

    private fun updateNotification() {
        startForeground(
            TIMER_NOTIFICATION_ID,
            timerNotificationBuilder!!.build()
        )
    }

    private fun setupGraph() {
        val build = DaggerTimeServiceComponent.builder()
            .behaviorTrackerAppComponent(BehaviorTrackerApp.getAppComponent(baseContext!!))
            .timeServiceModule(TimeServiceModule(this))
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
            PendingIntent.FLAG_UPDATE_CURRENT
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
            channelImportance
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
            val intent = Intent(context, TimeService::class.java)
            intent.action = ACTION_START
            ContextCompat.startForegroundService(context, intent)
        }

        fun dismissNotification(context: Context) {
            val intent = Intent(context, TimeService::class.java)
            intent.action = ACTION_DISMISS
            context.startService(intent)
        }
    }
}
