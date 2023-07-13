package fr.bowser.behaviortracker.choose_pomodoro_timer_view

import fr.bowser.behaviortracker.pomodoro.PomodoroManager
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.timer_repository.TimerRepository

class ChoosePomodoroTimerViewPresenter(
    private val screen: ChoosePomodoroTimerViewContract.Screen,
    private val timerRepository: TimerRepository,
    private val pomodoroManager: PomodoroManager
) : ChoosePomodoroTimerViewContract.Presenter {

    override fun onStart() {
        val timerList = timerRepository.getTimerList()
        screen.displayTimerList(timerList)
    }

    override fun onStop() {
        // nothing to do
    }

    override fun onTimerChose(timer: Timer) {
        pomodoroManager.startPomodoro(timer)
    }
}
