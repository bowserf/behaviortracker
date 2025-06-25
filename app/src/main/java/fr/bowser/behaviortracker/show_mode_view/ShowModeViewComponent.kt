package fr.bowser.behaviortracker.show_mode_view

import dagger.Component
import fr.bowser.behaviortracker.config.BehaviorTrackerAppComponent
import fr.bowser.behaviortracker.utils.GenericScope

@GenericScope(component = ShowModeViewComponent::class)
@Component(
    modules = arrayOf(ShowModeViewModule::class),
    dependencies = arrayOf(BehaviorTrackerAppComponent::class),
)
interface ShowModeViewComponent {

    fun inject(view: ShowModeViewFragment)

    fun provideShowModePresenter(): ShowModeViewContract.Presenter
}
