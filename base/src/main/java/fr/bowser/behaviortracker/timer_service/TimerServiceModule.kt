package fr.bowser.behaviortracker.timer_service

import android.content.Context
import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.pomodoro.PomodoroManager
import fr.bowser.behaviortracker.timer.TimerManager
import fr.bowser.behaviortracker.timer_repository.TimerRepository
import fr.bowser.behaviortracker.utils.GenericScope

@Module
class TimerServiceModule(private val screen: TimerServiceContract.Screen) {

    @GenericScope(component = TimerServiceComponent::class)
    @Provides
    fun provideTimerServicePresenter(
        context: Context,
        timeManager: TimerManager,
        timerRepository: TimerRepository,
        pomodoroManager: PomodoroManager
    ): TimerServiceContract.Presenter {
        return TimerServicePresenter(
            screen,
            timeManager,
            timerRepository,
            pomodoroManager,
            createAddOn(context)
        )
    }

    private fun createAddOn(context: Context): TimerServicePresenter.AddOn {
        return object : TimerServicePresenter.AddOn {
            override fun getNotificationName(timerName: String): String {
                return context.resources.getString(R.string.timer_service_pomodoro_title, timerName)
            }
        }
    }
}
