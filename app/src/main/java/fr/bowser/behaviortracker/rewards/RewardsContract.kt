package fr.bowser.behaviortracker.rewards

import fr.bowser.behaviortracker.inapp.InApp

interface RewardsContract {

    interface Screen {

        fun displayListInApps(inApps: List<InApp>)

        fun displaySuccessPurchaseMessage()

        fun displayFailPurchaseMessage()

    }

    interface Presenter {

        fun start()

        fun stop()

    }

}