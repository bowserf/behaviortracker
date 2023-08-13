package fr.bowser.feature_review

import android.content.Context
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.review.testing.FakeReviewManager
import fr.bowser.feature_review.internal.ReviewManagerImpl

class ReviewModule {

    companion object {

        fun createReviewManager(
            context: Context,
            isDebug: Boolean
        ): ReviewManager {
            val reviewManager = if (isDebug) {
                /*
                 * Google documentation "In-app reviews require your app to be published in Play Store.
                 * However, you can test your integration without publishing your app to production
                 * using either internal test tracks or internal app sharing. Both methods are
                 * described in this section."
                 *
                 * https://developer.android.com/guide/playcore/in-app-review/test#test-play-store
                 */
                FakeReviewManager(context)
            } else {
                ReviewManagerFactory.create(context)
            }
            return ReviewManagerImpl(reviewManager)
        }
    }
}
