package fr.bowser.behaviortracker.explain_permission_request

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
import javax.inject.Inject

class ExplainPermissionRequestFragment : Fragment(R.layout.fragment_explain_permission_request) {

    @Inject
    lateinit var presenter: ExplainPermissionRequestContract.Presenter

    private val screen = createScreen()

    private lateinit var titleTv: TextView
    private lateinit var messageTv: TextView
    private lateinit var iconImg: ImageView

    private val activityResultLauncher = createActivityResultLauncher()

    private val args: ExplainPermissionRequestFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupGraph()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        titleTv = view.findViewById(R.id.explain_permission_request_view_title)
        messageTv = view.findViewById(R.id.explain_permission_request_view_message)
        iconImg = view.findViewById(R.id.explain_permission_request_view_icon)

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
        val build = DaggerExplainPermissionRequestComponent.builder()
            .behaviorTrackerAppComponent(BehaviorTrackerApp.getAppComponent(requireContext()))
            .explainPermissionRequestModule(
                ExplainPermissionRequestModule(
                    screen,
                    args.explainPermissionRequestModel
                )
            )
            .build()
        build.inject(this)
    }

    private fun createScreen() = object : ExplainPermissionRequestContract.Screen {
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
