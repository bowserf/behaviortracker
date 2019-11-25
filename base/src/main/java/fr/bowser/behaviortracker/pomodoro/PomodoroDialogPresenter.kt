package fr.bowser.behaviortracker.pomodoro

class PomodoroDialogPresenter(private val pomodoroManager: PomodoroManager) :
    PomodoroDialogContract.Presenter {

    override fun onClickPositionButton() {
        pomodoroManager.resume()
    }

    override fun onClickNegativeButton() {
        pomodoroManager.stop()
    }
}