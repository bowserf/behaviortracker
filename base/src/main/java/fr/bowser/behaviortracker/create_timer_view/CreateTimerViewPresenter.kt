package fr.bowser.behaviortracker.create_timer_view

import fr.bowser.behaviortracker.event.EventManager
import fr.bowser.behaviortracker.pomodoro.PomodoroManager
import fr.bowser.behaviortracker.time_provider.TimeProvider
import fr.bowser.behaviortracker.timer.TimerManager
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.timer_list.TimerListManager
import fr.bowser.behaviortracker.utils.ColorUtils

class CreateTimerViewPresenter(
    private val screen: CreateTimerViewContract.Screen,
    private val timerListManager: TimerListManager,
    private val pomodoroManager: PomodoroManager,
    private val eventManager: EventManager,
    private val timeProvider: TimeProvider,
    private val timeManager: TimerManager
) : CreateTimerViewContract.Presenter {

    private var colorPosition: Int = 0

    private var isPomodoroMode = false

    private var isColorDisplayed = false

    private var isTimeDisplayed = false

    init {
        colorPosition = computeSelectedColorPosition()
    }

    override fun onStart() {
        screen.fillColorList(colorPosition)
        screen.updateContainerColorState(isColorDisplayed)
        screen.updateContainerTimeState(isTimeDisplayed)
    }

    override fun onStop() {
        // nothing to do
    }

    override fun onClickColor(oldSelectedPosition: Int, selectedPosition: Int) {
        colorPosition = selectedPosition
        screen.updateColorList(oldSelectedPosition, selectedPosition)
    }

    override fun onClickCreateTimer(name: String, hour: Int, minute: Int, startNow: Boolean) {
        if (name.isEmpty()) {
            screen.displayNameError()
            return
        }

        val currentTime = (hour * 3600 + minute * 60).toLong()
        val createDateTimestamp = timeProvider.getCurrentTimeMs()
        val timer = Timer(
            name,
            ColorUtils.convertPositionToColor(colorPosition),
            currentTime = currentTime,
            creationDateTimestamp = createDateTimestamp,
            lastUpdateTimestamp = createDateTimestamp
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

    override fun onClickChangeColorState() {
        isColorDisplayed = !isColorDisplayed
        screen.updateContainerColorState(isColorDisplayed)
    }

    override fun onClickChangeTimeState() {
        isTimeDisplayed = !isTimeDisplayed
        screen.updateContainerTimeState(isTimeDisplayed)
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
