package fr.bowser.behaviortracker.home


import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(HomeModule::class))
interface HomeComponent {

    fun inject(activity: HomeActivity)

    fun provideHomePresenter(): HomePresenter

}
