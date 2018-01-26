package fr.bowser.behaviortracker.timerlist

class TimerPresenter(private val view: TimerContract.View)
    : TimerContract.Presenter {

    override fun onClickAddTimer() {
        view.displayCreateTimerView()
    }

}