package fr.bowser.behaviortracker.home

class HomePresenter(private val view: HomeContract.View) : HomeContract.Presenter {

    override fun start() {
        view.displayTimerView()
    }

    override fun onClickTimer() {
        view.displayTimerView()
    }

    override fun onClickTimeline() {
        view.displayTimelineView()
    }


}