package fr.bowser.behaviortracker.review

import android.content.SharedPreferences
import androidx.core.content.edit

class ReviewStorageImpl(
    private val sharedPreferences: SharedPreferences,
) : ReviewStorage {

    override fun isReviewMarked(): Boolean {
        return sharedPreferences.getBoolean(PREF_KEY_MARK_REVIEW, false)
    }

    override fun markReview() {
        sharedPreferences.edit { putBoolean(PREF_KEY_MARK_REVIEW, true) }
    }

    companion object {
        const val PREFERENCES_NAME = "review_storage.pref"
        private const val PREF_KEY_MARK_REVIEW = "pref.key.mark_review"
    }
}
