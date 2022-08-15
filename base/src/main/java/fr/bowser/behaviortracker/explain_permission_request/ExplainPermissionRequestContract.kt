package fr.bowser.behaviortracker.explain_permission_request

import androidx.annotation.DrawableRes

interface ExplainPermissionRequestContract {

    interface Presenter {

        fun onAttach()

        fun onDetach()

        fun onClickNoThanks()

        fun onClickRequest()

        fun onPermissionStatusChanged(permissionToStatus: Map<String, Boolean>)
    }

    interface Screen {
        fun requestPermission(permissions: List<String>)

        fun displayPermissionInformation(title: String, message: String, @DrawableRes icon: Int)

        fun leaveScreen()
    }
}
