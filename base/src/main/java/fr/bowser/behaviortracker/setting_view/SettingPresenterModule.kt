package fr.bowser.behaviortracker.setting_view

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.utils.GenericScope

@Module
class SettingPresenterModule {

    @GenericScope(component = SettingComponent::class)
    @Provides
    fun provideSettingPresenter(): SettingContract.Presenter {
        return SettingPresenter(
        )
    }
}
