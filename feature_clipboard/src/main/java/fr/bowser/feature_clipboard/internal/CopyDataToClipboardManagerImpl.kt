package fr.bowser.feature_clipboard.internal

import android.content.ClipData
import android.content.ClipboardManager
import fr.bowser.feature_clipboard.CopyDataToClipboardManager
import fr.bowser.feature_clipboard.R
import fr.bowser.feature_string.StringManager

internal class CopyDataToClipboardManagerImpl(
    private val stringManager: StringManager,
    private val clipboardManager: ClipboardManager
) : CopyDataToClipboardManager {

    override fun copy(data: String) {
        val label = stringManager.getString(R.string.copy_data_to_clipboard_label)
        val clipData = ClipData.newPlainText(label, data)
        clipboardManager.setPrimaryClip(clipData)
    }
}
