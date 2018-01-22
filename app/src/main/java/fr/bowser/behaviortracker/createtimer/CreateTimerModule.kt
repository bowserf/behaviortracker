package fr.bowser.behaviortracker.createtimer

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.database.DatabaseManager
import fr.bowser.behaviortracker.utils.GenericScope

@Module
class CreateTimerModule {

    @GenericScope(component = CreateTimerComponent::class)
    @Provides
    fun provideCreateTimerPresenter(database: DatabaseManager) : CreateTimerPresenter {
        return CreateTimerPresenter(database.provideTimerDAO())
    }

}