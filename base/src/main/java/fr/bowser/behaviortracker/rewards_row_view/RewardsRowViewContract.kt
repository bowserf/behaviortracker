package fr.bowser.behaviortracker.rewards_row_view

import android.app.Activity

interface RewardsRowViewContract {

    interface Presenter {

        fun onItemClicked(sku: String)
    }

    interface Screen {

        fun getActivity(): Activity

        fun displayStoreConnectionError()
    }
}
