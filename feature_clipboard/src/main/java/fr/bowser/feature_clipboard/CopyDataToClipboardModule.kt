package fr.bowser.feature_clipboard

import android.content.ClipboardManager
import android.content.Context
import fr.bowser.feature_clipboard.internal.CopyDataToClipboardManagerImpl
import fr.bowser.feature_string.StringManager

class CopyDataToClipboardModule(
    private val context: Context,
    private val stringManager: StringManager
) {

    fun createCopyDataManager(): CopyDataToClipboardManager {
        val clipboardManager = context.getSystemService(
            Context.CLIPBOARD_SERVICE
        ) as ClipboardManager
        return CopyDataToClipboardManagerImpl(
            stringManager,
            clipboardManager
        )
    }
}
