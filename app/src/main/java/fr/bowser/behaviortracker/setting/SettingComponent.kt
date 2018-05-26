package fr.bowser.behaviortracker.setting

import dagger.Component
import fr.bowser.behaviortracker.config.BehaviorTrackerAppComponent
import fr.bowser.behaviortracker.utils.GenericScope

@GenericScope(component = SettingComponent::class)
@Component(
        modules = [(SettingPresenterModule::class)],
        dependencies = [(BehaviorTrackerAppComponent::class)])
interface SettingComponent {

    fun inject(fragment: SettingFragment)

    fun provideSettingPresenter(): SettingPresenter

}