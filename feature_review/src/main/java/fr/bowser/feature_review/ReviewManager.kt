package fr.bowser.feature_review

// https://developer.android.com/guide/playcore/in-app-review/kotlin-java
interface ReviewManager {

    fun launchReviewFlow(activityContainer: ReviewActivityContainer)

    fun addListener(listener: Listener)

    fun removeListener(listener: Listener)

    interface Listener {

        fun onSucceeded()

        fun onFailed(failReason: FailReason)
    }

    enum class FailReason {
        ACTIVITY_NOT_READY,
        REQUEST_REVIEW_FLOW_FAILED,
        REVIEW_FLOW_FAILED,
    }
}
