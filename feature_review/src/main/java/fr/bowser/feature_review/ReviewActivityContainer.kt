package fr.bowser.feature_review

import android.app.Activity

interface ReviewActivityContainer {

    fun isActivityAccessible(): Boolean

    fun getActivity(): Activity
}
