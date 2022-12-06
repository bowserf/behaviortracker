package fr.bowser.behaviortracker.setting_view

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.utils.GenericScope

@Module
class SettingViewModule {

    @GenericScope(component = SettingViewComponent::class)
    @Provides
    fun provideSettingPresenter(): SettingViewContract.Presenter {
        return SettingViewPresenter()
    }
}
