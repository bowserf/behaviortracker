package fr.bowser.behaviortracker.pomodoro_view

import fr.bowser.behaviortracker.pomodoro.PomodoroManager

class PomodoroDialogPresenter(
    private val pomodoroManager: PomodoroManager
) : PomodoroDialogContract.Presenter {

    override fun onClickPositionButton() {
        pomodoroManager.resume()
    }

    override fun onClickNegativeButton() {
        pomodoroManager.stop()
    }
}
