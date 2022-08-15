package fr.bowser.behaviortracker.explain_permission_request

import dagger.Component
import fr.bowser.behaviortracker.config.BehaviorTrackerAppComponent
import fr.bowser.behaviortracker.utils.GenericScope

@GenericScope(component = ExplainPermissionRequestComponent::class)
@Component(
    modules = [(ExplainPermissionRequestModule::class)],
    dependencies = [(BehaviorTrackerAppComponent::class)]
)
interface ExplainPermissionRequestComponent {

    fun inject(fragment: ExplainPermissionRequestFragment)
}
