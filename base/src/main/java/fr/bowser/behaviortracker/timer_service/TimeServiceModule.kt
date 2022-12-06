package fr.bowser.behaviortracker.timer_service

import android.content.Context
import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.instantapp.InstantAppManager
import fr.bowser.behaviortracker.pomodoro.PomodoroManager
import fr.bowser.behaviortracker.timer.TimerManager
import fr.bowser.behaviortracker.timer_list.TimerListManager
import fr.bowser.behaviortracker.utils.GenericScope

@Module
class TimeServiceModule(private val screen: TimeServiceContract.Screen) {

    @GenericScope(component = TimeServiceComponent::class)
    @Provides
    fun provideTimeServicePresenter(
        context: Context,
        instantAppManager: InstantAppManager,
        timeManager: TimerManager,
        timerListManager: TimerListManager,
        pomodoroManager: PomodoroManager
    ): TimeServiceContract.Presenter {
        return TimeServicePresenter(
            screen,
            instantAppManager,
            timeManager,
            timerListManager,
            pomodoroManager,
            createAddOn(context)
        )
    }

    private fun createAddOn(context: Context): TimeServicePresenter.AddOn {
        return object : TimeServicePresenter.AddOn {
            override fun getNotificationName(timerName: String): String {
                return context.resources.getString(R.string.timer_service_pomodoro_title, timerName)
            }
        }
    }
}
