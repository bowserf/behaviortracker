package fr.bowser.behaviortracker.showmodeitem

import android.content.Context
import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.timer.TimeManager
import fr.bowser.behaviortracker.notification.TimeService
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.utils.GenericScope

@Module
class ShowModeTimerViewModule(private val screen: ShowModeTimerViewContract.Screen) {

    @GenericScope(component = ShowModeTimerViewComponent::class)
    @Provides
    fun provideShowModeTimerViewPresenter(
        context: Context,
        timeManager: TimeManager
    ): ShowModeTimerViewContract.Presenter {
        val addOn = createAddOn(context)
        return ShowModeTimerViewPresenter(screen, timeManager, addOn)
    }

    private fun createAddOn(context: Context): ShowModeTimerViewPresenter.AddOn {
        return object : ShowModeTimerViewPresenter.AddOn {
            override fun startTimer(timer: Timer) {
                TimeService.startTimer(context, timer.id)
            }

            override fun stopTimer(timer: Timer) {
                TimeService.stopTimer(context, timer.id)
            }

        }
    }
}