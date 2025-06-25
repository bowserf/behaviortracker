package fr.bowser.behaviortracker.speech_to_text

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment

interface SpeechToTextManager {

    fun startSpeechToText(activity: Activity)

    fun startSpeechToText(fragment: Fragment)

    fun getText(intent: Intent?): String?

    fun isRecognitionAvailable(): Boolean
}
