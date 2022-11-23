package fr.bowser.behaviortracker.floating_running_timer

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.utils.GenericScope

@Module
class FloatingRunningTimerModule(private val screen: FloatingRunningTimerContract.Screen) {

    @GenericScope(component = FloatingRunningTimerComponent::class)
    @Provides
    fun provideTimerItemPresenter(): FloatingRunningTimerContract.Presenter {
        return FloatingRunningTimerPresenter(
            screen
        )
    }
}
