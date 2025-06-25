package fr.bowser.behaviortracker.pomodoro_view_dialog

import fr.bowser.behaviortracker.pomodoro.PomodoroManager

class PomodoroViewDialogPresenter(
    private val pomodoroManager: PomodoroManager,
) : PomodoroViewDialogContract.Presenter {

    override fun onClickPositionButton() {
        pomodoroManager.resume()
    }

    override fun onClickNegativeButton() {
        pomodoroManager.stop()
    }
}
