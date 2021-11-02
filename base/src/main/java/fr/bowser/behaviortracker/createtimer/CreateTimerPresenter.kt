package fr.bowser.behaviortracker.createtimer

import fr.bowser.behaviortracker.event.EventManager
import fr.bowser.behaviortracker.pomodoro.PomodoroManager
import fr.bowser.behaviortracker.time.TimeProvider
import fr.bowser.behaviortracker.timer.TimeManager
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.timer.TimerListManager
import fr.bowser.behaviortracker.utils.ColorUtils

class CreateTimerPresenter(
    private val screen: CreateTimerContract.Screen,
    private val timerListManager: TimerListManager,
    private val timeManager: TimeManager,
    private val pomodoroManager: PomodoroManager,
    private val eventManager: EventManager,
    private val timeProvider: TimeProvider
) : CreateTimerContract.Presenter {

    private var colorPosition: Int = 0

    private var isPomodoroMode = false

    init {
        colorPosition = computeSelectedColorPosition()
    }

    override fun onStart() {
        screen.fillColorList(colorPosition)
    }

    override fun onStop() {
        // nothing to do
    }

    override fun changeSelectedColor(oldSelectedPosition: Int, selectedPosition: Int) {
        colorPosition = selectedPosition
        screen.updateColorList(oldSelectedPosition, selectedPosition)
    }

    override fun createTimer(name: String, startNow: Boolean) {
        if (name.isEmpty()) {
            screen.displayNameError()
            return
        }

        val currentTime = timeProvider.getCurrentTimeMs()
        val timer = Timer(
            name,
            ColorUtils.convertPositionToColor(colorPosition),
            currentTime,
            currentTime
        )
        timerListManager.addTimer(timer)

        eventManager.sendTimerCreateEvent(startNow)

        if (startNow) {
            timeManager.startTimer(timer)
        }

        if (isPomodoroMode) {
            pomodoroManager.startPomodoro(timer)
        }

        screen.exitViewAfterSucceedTimerCreation()
    }

    override fun enablePomodoroMode(isPomodoro: Boolean) {
        isPomodoroMode = isPomodoro
    }

    private fun computeSelectedColorPosition(): Int {
        val colors = timerListManager.getTimerList().map { it.color }.sorted()
        for (i in 0 until ColorUtils.NUMBER_COLORS) {
            if (!colors.contains(i)) {
                return i
            }
        }
        return 0
    }
}