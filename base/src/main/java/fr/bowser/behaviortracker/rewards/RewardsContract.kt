package fr.bowser.behaviortracker.rewards

import fr.bowser.behaviortracker.inapp.InApp

interface RewardsContract {

    interface Presenter {

        fun onStart()

        fun onStop()
    }

    interface Screen {

        fun displayListInApps(inApps: List<InApp>)

        fun displaySuccessPurchaseMessage()

        fun displayFailPurchaseMessage()
    }
}
