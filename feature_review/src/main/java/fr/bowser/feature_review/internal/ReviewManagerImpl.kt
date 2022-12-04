package fr.bowser.feature_review.internal

import fr.bowser.feature_review.ReviewActivityContainer
import fr.bowser.feature_review.ReviewManager

// https://developer.android.com/guide/playcore/in-app-review/kotlin-java
internal class ReviewManagerImpl(
    private val reviewManager: com.google.android.play.core.review.ReviewManager
) : ReviewManager {

    private val listeners = ArrayList<ReviewManager.Listener>()

    override fun launchReviewFlow(activityContainer: ReviewActivityContainer) {
        val requestTimestampMs = getTimestampMs()
        reviewManager.requestReviewFlow().addOnCompleteListener { requestTask ->
            if (!requestTask.isSuccessful) {
                for (listener in listeners) {
                    listener.onFailed(ReviewManager.FailReason.REQUEST_REVIEW_FLOW_FAILED)
                }
                return@addOnCompleteListener
            }
            if (!activityContainer.isActivityAccessible()) {
                for (listener in listeners) {
                    listener.onFailed(ReviewManager.FailReason.ACTIVITY_NOT_READY)
                }
                return@addOnCompleteListener
            }
            val reviewInfo = requestTask.result
            reviewManager.launchReviewFlow(
                activityContainer.getActivity(),
                reviewInfo
            ).addOnCompleteListener { launchTask ->
                val currentTimestampMs = getTimestampMs()
                val durationMs = kotlin.math.abs(requestTimestampMs - currentTimestampMs)
                /*
                 * Google documentation "If an error occurs during the in-app review flow,
                 * do not inform the user or change your app's normal user flow. Continue your
                 * app's normal user flow after onComplete is called."
                 *
                 * That's an hack with the time to detect the rating failed.
                 */
                val succeeded = durationMs > 400 && launchTask.isSuccessful
                if (succeeded) {
                    for (listener in listeners) {
                        listener.onSucceeded()
                    }
                } else {
                    for (listener in listeners) {
                        listener.onFailed(ReviewManager.FailReason.REVIEW_FLOW_FAILED)
                    }
                }
            }
        }
    }

    override fun addListener(listener: ReviewManager.Listener) {
        if (listeners.contains(listener)) {
            return
        }
        listeners.add(listener)
    }

    override fun removeListener(listener: ReviewManager.Listener) {
        listeners.remove(listener)
    }

    private fun getTimestampMs(): Long {
        return System.currentTimeMillis()
    }
}
