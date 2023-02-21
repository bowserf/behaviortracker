package fr.bowser.behaviortracker.app.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.View
import android.widget.RemoteViews
import fr.bowser.behaviortracker.app.R
import fr.bowser.behaviortracker.config.BehaviorTrackerApp
import fr.bowser.behaviortracker.home_activity.HomeActivity
import fr.bowser.behaviortracker.home_activity.HomeActivity.Companion.ACTION_SELECT_POMODORO_TIMER
import fr.bowser.behaviortracker.pomodoro.PomodoroManager
import fr.bowser.behaviortracker.utils.TimeConverter

class PomodoroAppWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        updateAppWidgets(context, appWidgetManager, appWidgetIds)
    }

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (ACTION_TOGGLE_START_STOP_POMODORO == action) {
            handleActionStartStopTimer(context)
        } else {
            super.onReceive(context, intent)
        }
    }

    private fun updateAppWidgets(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val pomodoroManager = BehaviorTrackerApp.getAppComponent(context).providePomodoroManager()
        if (pomodoroManager.isStarted) {
            updateWidgetSessionStarted(pomodoroManager, context, appWidgetManager, appWidgetIds)
        } else {
            updateWidgetNoSession(context, appWidgetManager, appWidgetIds)
        }
    }

    private fun updateWidgetSessionStarted(
        pomodoroManager: PomodoroManager,
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val intentToggleStartStopTimer = Intent(context, PomodoroAppWidgetProvider::class.java)
        intentToggleStartStopTimer.action = ACTION_TOGGLE_START_STOP_POMODORO
        val pendingIntentToggleStartStopTime = PendingIntent.getBroadcast(
            context,
            0,
            intentToggleStartStopTimer,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }
        )

        val views = RemoteViews(context.packageName, R.layout.widget_pomodoro)
        views.setOnClickPendingIntent(
            R.id.widget_pomodoro_start_stop,
            pendingIntentToggleStartStopTime
        )

        val startStopResource =
            if (pomodoroManager.isRunning) R.drawable.widget_pause_state else R.drawable.widget_play_state
        views.setImageViewResource(R.id.widget_pomodoro_start_stop, startStopResource)
        val currentPomodoroTime =
            TimeConverter.convertSecondsToHumanTime(
                pomodoroManager.pomodoroTime,
                TimeConverter.DisplayHoursMode.Never
            )
        views.setTextViewText(R.id.widget_pomodoro_chrono, currentPomodoroTime)

        if (pomodoroManager.isPendingState) {
            val stringRes =
                if (pomodoroManager.isBreakStep()) R.string.widget_pomodoro_click_to_start_break_session
                else R.string.widget_pomodoro_click_to_start_pomodoro_session
            views.setTextViewText(
                R.id.widget_pomodoro_timer_name,
                context.getString(stringRes)
            )
        } else {
            views.setTextViewText(
                R.id.widget_pomodoro_timer_name,
                pomodoroManager.currentTimer!!.name
            )
        }

        views.setViewVisibility(R.id.widget_pomodoro_container_session_started, View.VISIBLE)
        views.setViewVisibility(R.id.widget_pomodoro_container_no_session, View.GONE)

        appWidgetManager.updateAppWidget(appWidgetIds, views)
    }

    private fun updateWidgetNoSession(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val intentSelectPomodoroTimer = Intent(context, HomeActivity::class.java)
        intentSelectPomodoroTimer.action = ACTION_SELECT_POMODORO_TIMER
        val pendingIntentSelectPomodoroTimer = PendingIntent.getActivity(
            context,
            0,
            intentSelectPomodoroTimer,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }
        )

        val views = RemoteViews(context.packageName, R.layout.widget_pomodoro)
        views.setOnClickPendingIntent(
            R.id.widget_pomodoro_select_timer,
            pendingIntentSelectPomodoroTimer
        )

        views.setViewVisibility(R.id.widget_pomodoro_container_session_started, View.GONE)
        views.setViewVisibility(R.id.widget_pomodoro_container_no_session, View.VISIBLE)

        appWidgetManager.updateAppWidget(appWidgetIds, views)
    }

    private fun handleActionStartStopTimer(context: Context) {
        val pomodoroManager = BehaviorTrackerApp.getAppComponent(context).providePomodoroManager()
        if (pomodoroManager.isRunning) {
            pomodoroManager.pause()
        } else {
            pomodoroManager.resume()
        }
    }

    companion object {
        private const val ACTION_TOGGLE_START_STOP_POMODORO =
            "timer_app_widget_provider.action.toggle_start_stop_pomodoro"

        fun update(context: Context) {
            val componentName =
                ComponentName(context.packageName, PomodoroAppWidgetProvider::class.java.name)
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(componentName)

            val intent = Intent(context, PomodoroAppWidgetProvider::class.java)
            intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
            context.sendBroadcast(intent)
        }
    }
}
