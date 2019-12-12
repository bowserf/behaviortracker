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
import fr.bowser.behaviortracker.home.HomeActivity
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

        notificationManager = baseContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

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

        startForeground(
            TIMER_NOTIFICATION_ID,
            timerNotificationBuilder!!.build()
        )
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
        if (action == ACTION_START_TIMER) {
            updateNotification()
            val timerId = extractTimerIdFromExtra(intent)
            presenter.startTimer(timerId)
        } else if (action == ACTION_STOP_TIMER) {
            updateNotification()
            val timerId = extractTimerIdFromExtra(intent)
            presenter.stopTimer(timerId)
        } else if (action == ACTION_PAUSE_TIMER) {
            updateNotification()
            presenter.pauseTimer()
        } else if (action == ACTION_RESUME_TIMER) {
            updateNotification()
            presenter.resumeTimer()
        }else if (action == ACTION_DISMISS) {
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

    private fun extractTimerIdFromExtra(intent: Intent): Long {
        val timerId = intent.getLongExtra(EXTRA_TIMER_ID, -1)
        check(timerId != -1L) { "No timer ID has been send." }
        return timerId
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

        private const val ACTION_START_TIMER = "time_service.action.start_timer"
        private const val ACTION_STOP_TIMER = "time_service.action.stop_timer"
        private const val ACTION_RESUME_TIMER = "time_service.action.resume_timer"
        private const val ACTION_PAUSE_TIMER = "time_service.action.pause_timer"
        private const val ACTION_DISMISS = "time_service.action.dismiss"

        private const val EXTRA_TIMER_ID = "timer_service_extra_timer_id"

        fun startTimer(context: Context, timerId: Long) {
            val intent = Intent(context, TimeService::class.java)
            intent.action = ACTION_START_TIMER
            intent.putExtra(EXTRA_TIMER_ID, timerId)
            ContextCompat.startForegroundService(context, intent)
        }

        fun stopTimer(context: Context, timerId: Long) {
            val intent = Intent(context, TimeService::class.java)
            intent.action = ACTION_STOP_TIMER
            intent.putExtra(EXTRA_TIMER_ID, timerId)
            ContextCompat.startForegroundService(context, intent)
        }

        fun resumeTimer(context: Context) {
            val intent = Intent(context, TimeService::class.java)
            intent.action = ACTION_RESUME_TIMER
            ContextCompat.startForegroundService(context, intent)
        }

        fun pauseTimer(context: Context) {
            val intent = Intent(context, TimeService::class.java)
            intent.action = ACTION_PAUSE_TIMER
            ContextCompat.startForegroundService(context, intent)
        }

        fun dismissNotification(context: Context) {
            val intent = Intent(context, TimeService::class.java)
            intent.action = ACTION_DISMISS
            context.startService(intent)
        }
    }
}