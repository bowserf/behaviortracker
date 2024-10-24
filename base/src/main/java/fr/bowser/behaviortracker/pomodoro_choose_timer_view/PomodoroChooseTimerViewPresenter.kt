package fr.bowser.behaviortracker.pomodoro_choose_timer_view

import fr.bowser.behaviortracker.pomodoro.PomodoroManager
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.timer_repository.TimerRepository

class PomodoroChooseTimerViewPresenter(
    private val screen: PomodoroChooseTimerViewContract.Screen,
    private val timerRepository: TimerRepository,
    private val pomodoroManager: PomodoroManager,
) : PomodoroChooseTimerViewContract.Presenter {

    override fun onStart() {
        val timerList = timerRepository.getTimerNotFinished()
        screen.displayTimerList(timerList)
    }

    override fun onStop() {
        // nothing to do
    }

    override fun onTimerChose(timer: Timer) {
        pomodoroManager.startPomodoro(timer)
    }
}
