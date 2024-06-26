package fr.bowser.behaviortracker.speech_to_text.internal

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.fragment.app.Fragment
import fr.bowser.behaviortracker.speech_to_text.SpeechToTextManager
import java.util.Locale

internal class SpeechToTextManagerImpl(
    private val context: Context,
) : SpeechToTextManager {

    override fun startSpeechToText(activity: Activity) {
        val intent = createIntent()
        activity.startActivityForResult(intent, REQUEST_CODE)
    }

    override fun startSpeechToText(fragment: Fragment) {
        val intent = createIntent()
        fragment.startActivityForResult(intent, REQUEST_CODE)
    }

    override fun getText(intent: Intent?): String? {
        val nonNullIntent = intent ?: return null
        val bundle = nonNullIntent.extras ?: return null
        val mostLikelyCandidates =
            bundle.getStringArrayList(RecognizerIntent.EXTRA_RESULTS) ?: return null
        return mostLikelyCandidates.first()
    }

    override fun isRecognitionAvailable() = SpeechRecognizer.isRecognitionAvailable(context)

    private fun createIntent(): Intent {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM,
        )
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, ENABLE_PARTIAL_RESULT)
        // Doesn't seems to end the record after the specified time...
        // intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 1_500)
        // We don't want to keep the micro "on" if it's only to orally say "ok"
        // intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 2_000)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, MAX_NUMBER_RESULTS)
        return intent
    }

    companion object {
        const val REQUEST_CODE = 124
        private const val ENABLE_PARTIAL_RESULT = true
        private const val MAX_NUMBER_RESULTS = 1
    }
}
