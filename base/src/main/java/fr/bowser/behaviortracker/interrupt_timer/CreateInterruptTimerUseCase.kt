package fr.bowser.behaviortracker.interrupt_timer

import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.event.EventManager
import fr.bowser.behaviortracker.time_provider.TimeProvider
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.timer.TimerManager
import fr.bowser.behaviortracker.timer_repository.TimerRepository
import fr.bowser.behaviortracker.utils.ColorUtils
import fr.bowser.feature_string.StringManager
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone


interface CreateInterruptTimerUseCase {

    operator fun invoke()
}

class CreateInterruptTimerUseCaseImpl(
    private val eventManager: EventManager,
    private val timeProvider: TimeProvider,
    private val timerRepository: TimerRepository,
    private val timerManager: TimerManager,
    private val stringManager: StringManager,
) : CreateInterruptTimerUseCase {

    private val formatter by lazy {
        SimpleDateFormat("HH:mm:ss", Locale.getDefault()).apply {
            timeZone = TimeZone.getDefault()
        }
    }

    override fun invoke() {
        val createDateTimestamp = timeProvider.getCurrentTimeMs()
        val date = getHumanReadableDate(createDateTimestamp)
        val timer = Timer(
            name = stringManager.getString(R.string.timer_interrupt_name, date),
            color = ColorUtils.COLOR_GREY,
            creationDateTimestamp = createDateTimestamp,
            lastUpdateTimestamp = createDateTimestamp
        )
        timerRepository.addTimer(timer)
        timerManager.startTimer(timer)
        eventManager.sendInterruptTimerCreateEvent()
    }

    private fun getHumanReadableDate(timestamp: Long): String {
        val date = Date(timestamp)
        return formatter.format(date)
    }
}
