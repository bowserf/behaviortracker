package fr.bowser.behaviortracker.setting_view

import android.content.Context
import android.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.event.EventManager
import fr.bowser.behaviortracker.utils.GenericScope
import fr.bowser.feature_string.StringManager

@Module
class SettingPresenterModule {

    @GenericScope(component = SettingComponent::class)
    @Provides
    fun provideSettingPresenter(
        context: Context,
        stringManager: StringManager,
        eventManager: EventManager
    ): SettingContract.Presenter {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return SettingPresenter(
            sharedPreferences,
            stringManager,
            eventManager
        )
    }
}