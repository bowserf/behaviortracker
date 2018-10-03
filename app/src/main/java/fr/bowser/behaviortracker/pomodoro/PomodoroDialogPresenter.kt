package fr.bowser.behaviortracker.pomodoro

import fr.bowser.behaviortracker.timer.TimeManager

class PomodoroDialogPresenter(private val timeManager: TimeManager,
                              private val pomodoroManager: PomodoroManager) : PomodoroDialogContract.Presenter {

    override fun onClickPositionButton() {
        //timeManager.startTimer(pomodoroManager.currentTimer!!)
        pomodoroManager.resume()
    }

    override fun onClickNegativeButton() {
        pomodoroManager.stop()
    }


}