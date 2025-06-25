package fr.bowser.behaviortracker.review

interface ReviewStorage {

    fun isReviewMarked(): Boolean

    fun markReview()
}
