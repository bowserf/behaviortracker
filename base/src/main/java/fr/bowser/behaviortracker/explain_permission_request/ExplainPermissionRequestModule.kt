package fr.bowser.behaviortracker.explain_permission_request

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.utils.GenericScope

@Module
class ExplainPermissionRequestModule(
    private val screen: ExplainPermissionRequestContract.Screen,
    private val explainPermissionRequestModel: ExplainPermissionRequestModel
) {

    @GenericScope(component = ExplainPermissionRequestComponent::class)
    @Provides
    fun provideExplainPermissionRequestPresenter(
    ): ExplainPermissionRequestContract.Presenter {
        return ExplainPermissionRequestPresenter(screen, explainPermissionRequestModel)
    }
}
