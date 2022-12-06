package fr.bowser.behaviortracker.show_mode_item_view

import dagger.Component
import fr.bowser.behaviortracker.config.BehaviorTrackerAppComponent
import fr.bowser.behaviortracker.utils.GenericScope

@GenericScope(component = ShowModeItemViewComponent::class)
@Component(
    modules = arrayOf(ShowModeItemViewModule::class),
    dependencies = arrayOf(BehaviorTrackerAppComponent::class)
)
interface ShowModeItemViewComponent {

    fun inject(view: ShowModeItemView)

    fun provideShowModeTimerViewPresenter(): ShowModeItemViewContract.Presenter
}
