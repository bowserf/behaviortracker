package fr.bowser.behaviortracker.explain_permission_request

class ExplainPermissionRequestPresenter(
    private val screen: ExplainPermissionRequestContract.Screen,
    private val explainPermissionRequestModel: ExplainPermissionRequestModel
) : ExplainPermissionRequestContract.Presenter {

    override fun onAttach() {
        updateScreen()
    }

    override fun onDetach() {
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
            explainPermissionRequestModel.icon
        )
    }
}
