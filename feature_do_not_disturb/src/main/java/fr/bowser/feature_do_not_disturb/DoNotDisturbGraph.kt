package fr.bowser.feature_do_not_disturb

import android.content.Context
import fr.bowser.feature_do_not_disturb.internal.DoNotDisturbGraphInternal

class DoNotDisturbGraph {

    private val doNotDisturbManager by lazy { DoNotDisturbGraphInternal.getDoNotDisturbManager() }

    companion object {

        private var graph: DoNotDisturbGraph? = null

        fun init(context: Context) {
            if (graph == null) {
                graph = DoNotDisturbGraph()
                DoNotDisturbGraphInternal.init(context)
            }
        }

        fun getDoNotDisturbManager() = graph!!.doNotDisturbManager
    }
}
