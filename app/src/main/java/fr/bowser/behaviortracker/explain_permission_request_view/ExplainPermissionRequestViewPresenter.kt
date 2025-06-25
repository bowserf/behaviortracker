package fr.bowser.behaviortracker.explain_permission_request_view

class ExplainPermissionRequestViewPresenter(
    private val screen: ExplainPermissionRequestViewContract.Screen,
    private val explainPermissionRequestModel: ExplainPermissionRequestViewModel,
) : ExplainPermissionRequestViewContract.Presenter {

    override fun onAttach() {
        updateScreen()
    }

    override fun onDetach() {
        // nothing to do
    }

    override fun onClickNoThanks() {
        screen.leaveScreen()
    }

    override fun onClickRequest() {
        screen.requestPermission(explainPermissionRequestModel.permissions)
    }

    override fun onPermissionStatusChanged(permissionToStatus: Map<String, Boolean>) {
        screen.leaveScreen()
    }

    private fun updateScreen() {
        screen.displayPermissionInformation(
            explainPermissionRequestModel.title,
            explainPermissionRequestModel.message,
            explainPermissionRequestModel.icon,
        )
    }
}
