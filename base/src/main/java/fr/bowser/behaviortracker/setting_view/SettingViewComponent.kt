package fr.bowser.behaviortracker.setting_view

import dagger.Component
import fr.bowser.behaviortracker.config.BehaviorTrackerAppComponent
import fr.bowser.behaviortracker.utils.GenericScope

@GenericScope(component = SettingViewComponent::class)
@Component(
    modules = [(SettingViewModule::class)],
    dependencies = [(BehaviorTrackerAppComponent::class)]
)
interface SettingViewComponent {

    fun inject(fragment: SettingViewFragment)

    fun provideSettingPresenter(): SettingViewContract.Presenter
}
