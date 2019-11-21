package fr.bowser.behaviortracker.event

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

class EventManagerImpl(context: Context) : EventManager {

    private val firebaseAnalytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(context)

    override fun sendTimerCreateEvent(startNow: Boolean) {
        val bundle = Bundle()
        bundle.putBoolean(EVENT_EXTRA_TIMER_CREATED, startNow)
        firebaseAnalytics.logEvent(EVENT_TIMER_CREATED, bundle)
    }

    override fun sendExclusiveTimerModeEvent(isExclusive: Boolean) {
        val bundle = Bundle()
        bundle.putBoolean(EVENT_EXTRA_EXCLUSIVE_TIMER_MODE, isExclusive)
        firebaseAnalytics.logEvent(EVENT_EXCLUSIVE_TIMER_MODE, bundle)
    }

    override fun sendNewTimeFixTimerDurationEvent(newTimer: Int) {
        val bundle = Bundle()
        bundle.putInt(EVENT_EXTRA_NEW_TIMER_FIX_TIMER_DURATION, newTimer)
        firebaseAnalytics.logEvent(EVENT_NEW_TIMER_FIX_TIMER_DURATION, bundle)
    }

    override fun sendSetAlarmEvent(enable: Boolean) {
        val bundle = Bundle()
        bundle.putBoolean(EVENT_EXTRA_ENABLE_ALARM, enable)
        firebaseAnalytics.logEvent(EVENT_SET_ALARM, bundle)
    }

    override fun sendDisplayAlarmNotificationEvent() {
        firebaseAnalytics.logEvent(EVENT_ALARM_NOTIF_DISPLAYED, null)
    }

    override fun sendAlarmNotificationClickedEvent() {
        firebaseAnalytics.logEvent(EVENT_ALARM_NOTIF_CLICKED, null)
    }

    override fun sendClickBuyInAppEvent(sku: String) {
        val bundle = Bundle()
        bundle.putString(EVENT_EXTRA_CLICK_BUY_IN_APP, sku)
        firebaseAnalytics.logEvent(EVENT_CLICK_BUY_IN_APP, bundle)
    }

    companion object {
        const val EVENT_ALARM_NOTIF_CLICKED = "click_alarm_notif"

        const val EVENT_ALARM_NOTIF_DISPLAYED = "display_alarm_notif"

        const val EVENT_SET_ALARM = "set_alarm"
        const val EVENT_EXTRA_ENABLE_ALARM = "is_enable"

        const val EVENT_TIMER_CREATED = "timer_created"
        const val EVENT_EXTRA_TIMER_CREATED = "start_now"

        const val EVENT_EXCLUSIVE_TIMER_MODE = "exclusive_timer_mode"
        const val EVENT_EXTRA_EXCLUSIVE_TIMER_MODE = "is_exclusive"

        const val EVENT_NEW_TIMER_FIX_TIMER_DURATION = "new_timer_duration_modificator"
        const val EVENT_EXTRA_NEW_TIMER_FIX_TIMER_DURATION = "new_time"

        const val EVENT_CLICK_BUY_IN_APP = "click_buy_in_app"
        const val EVENT_EXTRA_CLICK_BUY_IN_APP = "product_id"
    }
}