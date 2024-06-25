package fr.bowser.behaviortracker.utils

import android.text.InputType
import android.view.inputmethod.EditorInfo
import android.widget.EditText

// To use this, do NOT set inputType on the EditText in the layout
fun EditText.setMultiLineCapSentencesAndDoneAction() {
    imeOptions = EditorInfo.IME_ACTION_DONE
    setRawInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES or InputType.TYPE_TEXT_FLAG_MULTI_LINE)
}
