package fr.bowser.behaviortracker.rewards

import fr.bowser.behaviortracker.inapp.InApp

interface RewardsContract {

    interface Screen {

        fun displayListInApps(inApps: List<InApp>)

    }

    interface Presenter {

        fun start()

        fun stop()

    }

}