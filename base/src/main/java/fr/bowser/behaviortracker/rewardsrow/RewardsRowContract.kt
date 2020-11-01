package fr.bowser.behaviortracker.rewardsrow

import android.app.Activity
import com.android.billingclient.api.SkuDetails
import fr.bowser.behaviortracker.inapp.InApp

interface RewardsRowContract {

    interface Screen {

        fun setInApp(inApp: InApp)

        fun getActivity(): Activity

        fun displayStoreConnectionError()
    }

    interface Presenter {

        fun onItemClicked(skuDetails: SkuDetails?)
    }
}