package fr.bowser.behaviortracker.explain_permission_request_view

import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.utils.GenericScope

@Module
class ExplainPermissionRequestViewModule(
    private val screen: ExplainPermissionRequestViewContract.Screen,
    private val explainPermissionRequestModel: ExplainPermissionRequestViewModel,
) {

    @GenericScope(component = ExplainPermissionRequestViewComponent::class)
    @Provides
    fun provideExplainPermissionRequestViewPresenter(): ExplainPermissionRequestViewContract.Presenter {
        return ExplainPermissionRequestViewPresenter(screen, explainPermissionRequestModel)
    }
}
