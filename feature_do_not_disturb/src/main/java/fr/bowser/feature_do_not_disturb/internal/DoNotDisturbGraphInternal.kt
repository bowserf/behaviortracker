package fr.bowser.feature_do_not_disturb.internal

import android.content.Context

internal class DoNotDisturbGraphInternal(private val context: Context) {

    private val doNotDisturbManager by lazy { DoNotDisturbModule().createDoNotDisturbManager() }

    companion object {

        private var graph: DoNotDisturbGraphInternal? = null

        fun init(context: Context) {
            if (graph == null) {
                graph = DoNotDisturbGraphInternal(context)
            }
        }

        internal fun getContext() = graph!!.context
        fun getDoNotDisturbManager() = graph!!.doNotDisturbManager
    }
}
