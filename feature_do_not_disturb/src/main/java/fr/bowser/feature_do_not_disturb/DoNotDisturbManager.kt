package fr.bowser.feature_do_not_disturb

interface DoNotDisturbManager {

    fun isNotificationPolicyAccess(): DnDPolicyAccess

    fun askPermissionIfNeeded()

    fun getDnDState(): DnDState

    fun setDnDState(state: DnDState)

    fun addListener(listener: Listener)

    fun removeListener(listener: Listener)

    interface Listener {
        fun onDndStateChanged()
    }

    enum class DnDState {
        NONE, ALL, PRIORITY, ALARMS, UNKNOWN, NOT_MANAGED
    }

    enum class DnDPolicyAccess {
        GRANTED, DECLINED, NOT_MANAGED
    }
}
