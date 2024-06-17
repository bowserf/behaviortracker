package fr.bowser.behaviortracker.explain_permission_request_view

import dagger.Component
import fr.bowser.behaviortracker.config.BehaviorTrackerAppComponent
import fr.bowser.behaviortracker.utils.GenericScope

@GenericScope(component = ExplainPermissionRequestViewComponent::class)
@Component(
    modules = [(ExplainPermissionRequestViewModule::class)],
    dependencies = [(BehaviorTrackerAppComponent::class)],
)
interface ExplainPermissionRequestViewComponent {

    fun inject(fragment: ExplainPermissionRequestViewFragment)
}
