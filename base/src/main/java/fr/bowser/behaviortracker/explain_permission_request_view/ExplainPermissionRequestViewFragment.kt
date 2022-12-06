package fr.bowser.behaviortracker.explain_permission_request_view

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import fr.bowser.behaviortracker.R
import fr.bowser.behaviortracker.config.BehaviorTrackerApp
import fr.bowser.behaviortracker.utils.FragmentExtension.bind
import javax.inject.Inject

class ExplainPermissionRequestViewFragment : Fragment(R.layout.explain_permission_request_view_fragment) {

    @Inject
    lateinit var presenter: ExplainPermissionRequestViewContract.Presenter

    private val screen = createScreen()

    private val titleTv: TextView by bind(R.id.explain_permission_request_view_title)
    private val messageTv: TextView by bind(R.id.explain_permission_request_view_message)
    private val iconImg: ImageView by bind(R.id.explain_permission_request_view_icon)

    private val activityResultLauncher = createActivityResultLauncher()

    private val args: ExplainPermissionRequestViewFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupGraph()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<View>(R.id.explain_permission_request_view_cancel)
            .setOnClickListener { presenter.onClickNoThanks() }
        view.findViewById<View>(R.id.explain_permission_request_view_request)
            .setOnClickListener { presenter.onClickRequest() }
    }

    override fun onStart() {
        super.onStart()
        presenter.onAttach()
    }

    override fun onStop() {
        presenter.onDetach()
        super.onStop()
    }

    private fun createActivityResultLauncher() =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissionToStatus ->
            presenter.onPermissionStatusChanged(permissionToStatus)
        }

    private fun setupGraph() {
        val build = DaggerExplainPermissionRequestViewComponent.builder()
            .behaviorTrackerAppComponent(BehaviorTrackerApp.getAppComponent(requireContext()))
            .explainPermissionRequestViewModule(
                ExplainPermissionRequestViewModule(
                    screen,
                    args.explainPermissionRequestModel
                )
            )
            .build()
        build.inject(this)
    }

    private fun createScreen() = object : ExplainPermissionRequestViewContract.Screen {
        override fun requestPermission(permissions: List<String>) {
            activityResultLauncher.launch(permissions.toTypedArray())
        }

        override fun displayPermissionInformation(
            title: String,
            message: String,
            @DrawableRes icon: Int
        ) {
            titleTv.text = title
            messageTv.text = message
            iconImg.setImageResource(icon)
        }

        override fun leaveScreen() {
            findNavController().popBackStack()
        }
    }
}
