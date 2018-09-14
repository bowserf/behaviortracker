package fr.bowser.behaviortracker.rewardsrow

import android.app.Activity
import fr.bowser.behaviortracker.inapp.InApp

interface RewardsRowContract {

    interface Screen {

        fun setInApp(inApp: InApp)

        fun getActivity(): Activity

    }

    interface Presenter {

        fun onItemClicked(sku: String)

    }

}