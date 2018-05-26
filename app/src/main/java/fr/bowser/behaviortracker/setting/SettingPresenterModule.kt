package fr.bowser.behaviortracker.setting

import android.content.Context
import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.event.EventManager
import fr.bowser.behaviortracker.utils.GenericScope

@Module
class SettingPresenterModule {

    @GenericScope(component = SettingComponent::class)
    @Provides
    fun provideSettingPresenter(context: Context, eventManager: EventManager): SettingPresenter {
        return SettingPresenter(context, eventManager)
    }

}