package fr.bowser.behaviortracker.app.app_initialization

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import fr.bowser.behaviortracker.app_initialization.AppInitialization
import fr.bowser.behaviortracker.app_initialization.AppInitializationComponent
import fr.bowser.behaviortracker.utils.GenericScope

@GenericScope(component = AppInitializationComponentImpl::class)
@Component(
    modules = [AppInitializationModule::class]
)
interface AppInitializationComponentImpl : AppInitializationComponent {

    override fun provideAppInitialization(): AppInitialization

    @Component.Builder
    interface Builder {

        fun build(): AppInitializationComponent

        @BindsInstance
        fun context(context: Context): Builder
    }
}
