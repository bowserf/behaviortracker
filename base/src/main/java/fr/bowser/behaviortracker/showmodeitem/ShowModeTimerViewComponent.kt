package fr.bowser.behaviortracker.showmodeitem

import dagger.Component
import fr.bowser.behaviortracker.config.BehaviorTrackerAppComponent
import fr.bowser.behaviortracker.utils.GenericScope

@GenericScope(component = ShowModeTimerViewComponent::class)
@Component(
    modules = arrayOf(ShowModeTimerViewModule::class),
    dependencies = arrayOf(BehaviorTrackerAppComponent::class)
)
interface ShowModeTimerViewComponent {

    fun inject(view: ShowModeTimerView)

    fun provideShowModeTimerViewPresenter(): ShowModeTimerViewContract.Presenter
}
