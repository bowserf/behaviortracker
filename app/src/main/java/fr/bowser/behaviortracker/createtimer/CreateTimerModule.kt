package fr.bowser.behaviortracker.createtimer

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.database.DatabaseManager
import fr.bowser.behaviortracker.utils.GenericScope

@Module
class CreateTimerModule {

    @GenericScope(component = CreateTimerComponent::class)
    @Provides
    fun provideCreateTimerManager(databaseManager: DatabaseManager): CreateTimerManager{
        return CreateTimerManager(databaseManager.provideTimerDAO())
    }

    @GenericScope(component = CreateTimerComponent::class)
    @Provides
    fun provideCreateTimerPresenter(manager: CreateTimerManager) : CreateTimerPresenter {
        return CreateTimerPresenter(manager)
    }

}