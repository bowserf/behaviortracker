package fr.bowser.behaviortracker.review

import android.content.Context
import dagger.Module
import dagger.Provides
import fr.bowser.behaviortracker.BuildConfig
import fr.bowser.feature_review.ReviewManager
import javax.inject.Singleton

@Module
class ReviewModule {

    @Singleton
    @Provides
    fun provideReviewManager(context: Context): ReviewManager {
        return fr.bowser.feature_review.ReviewModule.createReviewManager(context, BuildConfig.DEBUG)
    }

    @Singleton
    @Provides
    fun provideReviewStorage(context: Context): ReviewStorage {
        val sharedPreferences = context.getSharedPreferences(
            ReviewStorageImpl.PREFERENCES_NAME,
            Context.MODE_PRIVATE,
        )
        return ReviewStorageImpl(sharedPreferences)
    }
}
