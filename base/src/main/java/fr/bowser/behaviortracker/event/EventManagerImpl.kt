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

    override fun sendInterruptTimerCreateEvent() {
        val bundle = Bundle()
        firebaseAnalytics.logEvent(EVENT_INTERRUPT_TIMER_CREATED, bundle)
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
        private const val EVENT_ALARM_NOTIF_CLICKED = "click_alarm_notif"

        private const val EVENT_ALARM_NOTIF_DISPLAYED = "display_alarm_notif"

        private const val EVENT_SET_ALARM = "set_alarm"
        private const val EVENT_EXTRA_ENABLE_ALARM = "is_enable"

        private const val EVENT_INTERRUPT_TIMER_CREATED = "interrupt_timer_created"

        private const val EVENT_TIMER_CREATED = "timer_created"
        private const val EVENT_EXTRA_TIMER_CREATED = "start_now"

        private const val EVENT_CLICK_BUY_IN_APP = "click_buy_in_app"
        private const val EVENT_EXTRA_CLICK_BUY_IN_APP = "product_id"
    }
}
