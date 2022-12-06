package fr.bowser.feature_do_not_disturb.internal

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.provider.Settings
import fr.bowser.feature_do_not_disturb.DoNotDisturbManager

internal class DoNotDisturbManagerImpl(
    private val context: Context
) : DoNotDisturbListenerManager {

    private val notificationManager =
        context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

    private val listeners = mutableListOf<DoNotDisturbManager.Listener>()

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val intent = IntentFilter()
            intent.addAction(NotificationManager.ACTION_INTERRUPTION_FILTER_CHANGED)
            context.registerReceiver(DoNotDisturbReceiver(), intent)
        }
    }

    override fun isNotificationPolicyAccess(): DoNotDisturbManager.DnDPolicyAccess {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return DoNotDisturbManager.DnDPolicyAccess.NOT_MANAGED
        }
        return if (notificationManager.isNotificationPolicyAccessGranted) DoNotDisturbManager.DnDPolicyAccess.GRANTED else DoNotDisturbManager.DnDPolicyAccess.DECLINED
    }

    @SuppressLint("InlinedApi")
    override fun askPermissionIfNeeded() {
        if (isNotificationPolicyAccess() == DoNotDisturbManager.DnDPolicyAccess.NOT_MANAGED) {
            return
        }
        val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.applicationContext.startActivity(intent)
    }

    override fun getDnDState(): DoNotDisturbManager.DnDState {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return DoNotDisturbManager.DnDState.NOT_MANAGED
        }
        return when (val currentInterruptionFilter = notificationManager.currentInterruptionFilter) {
            NotificationManager.INTERRUPTION_FILTER_NONE -> DoNotDisturbManager.DnDState.NONE
            NotificationManager.INTERRUPTION_FILTER_ALL -> DoNotDisturbManager.DnDState.ALL
            NotificationManager.INTERRUPTION_FILTER_PRIORITY -> DoNotDisturbManager.DnDState.PRIORITY
            NotificationManager.INTERRUPTION_FILTER_UNKNOWN -> DoNotDisturbManager.DnDState.UNKNOWN
            NotificationManager.INTERRUPTION_FILTER_ALARMS -> DoNotDisturbManager.DnDState.ALARMS
            else -> throw IllegalStateException("Not manager interruption filter state= $currentInterruptionFilter")
        }
    }

    override fun setDnDState(state: DoNotDisturbManager.DnDState) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return
        }
        val newState = when (state) {
            DoNotDisturbManager.DnDState.NONE -> NotificationManager.INTERRUPTION_FILTER_NONE
            DoNotDisturbManager.DnDState.ALL -> NotificationManager.INTERRUPTION_FILTER_ALL
            DoNotDisturbManager.DnDState.PRIORITY -> NotificationManager.INTERRUPTION_FILTER_PRIORITY
            DoNotDisturbManager.DnDState.ALARMS -> NotificationManager.INTERRUPTION_FILTER_ALARMS
            else -> throw IllegalStateException("You can't set DndState: $state")
        }
        notificationManager.setInterruptionFilter(newState)
    }

    override fun addListener(listener: DoNotDisturbManager.Listener) {
        if (listeners.contains(listener)) {
            return
        }
        listeners.add(listener)
    }

    override fun removeListener(listener: DoNotDisturbManager.Listener) {
        listeners.remove(listener)
    }

    override fun updateDndState() {
        listeners.forEach { it.onDndStateChanged() }
    }
}
