package fr.bowser.behaviortracker.timer

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fr.bowser.behaviortracker.R

class TimerFragment : Fragment() {

    companion object {
        const val TAG = "TimerFragment"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_timer, null)
    }

}