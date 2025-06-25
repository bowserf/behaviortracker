package fr.bowser.behaviortracker.utils

import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

fun Toolbar.applyStatusBarPadding() {
    // Apply padding to avoid drawing elements under status bar
    // No need to apply the same thing to BottomNavigationView because the element already
    // take the inset into account
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, windowInsets ->
        val insets = windowInsets.getInsets(WindowInsetsCompat.Type.statusBars())
        v.updatePadding(top = insets.top)
        // Return CONSUMED if you don't want want the window insets to keep passing
        // down to descendant views.
        WindowInsetsCompat.CONSUMED
    }
}
