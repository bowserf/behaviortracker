package fr.bowser.behaviortracker.notification

import android.content.Context
import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.pomodoro.PomodoroManager
import fr.bowser.behaviortracker.timer.TimeManager
import fr.bowser.behaviortracker.timer.TimerListManager
import fr.bowser.behaviortracker.utils.GenericScope

@Module
class TimeServiceModule(private val screen: TimeContract.Screen) {

    @GenericScope(component = TimeServiceComponent::class)
    @Provides
    fun provideTimeServicePresenter(
        context: Context,
        timeManager: TimeManager,
        timerListManager: TimerListManager,
        pomodoroManager: PomodoroManager
    ): TimeContract.Presenter {
        return TimeServicePresenter(
            screen,
            timeManager,
            timerListManager,
            pomodoroManager,
            createAddOn(context)
        )
    }

    private fun createAddOn(context: Context): TimeServicePresenter.AddOn {
        return object : TimeServicePresenter.AddOn {
            override fun getNotificationName(timerName: String): String {
                return context.resources.getString(R.string.pomodoro_title, timerName)

            }

        }
    }
}