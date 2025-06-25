package fr.bowser.behaviortracker.pomodoro

import fr.bowser.behaviortracker.pomodoro_view_dialog.PomodoroViewDialogPresenter
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PomodoroDialogPresenterTest {

    @Mock
    private lateinit var manager: PomodoroManager

    @Test
    fun clickOnPositionButtonResumeSession() {
        // Given
        val presenter = PomodoroViewDialogPresenter(manager)

        // When
        presenter.onClickPositionButton()

        // Then
        Mockito.verify(manager).resume()
    }

    @Test
    fun clickOnNegativeButtonStopSession() {
        // Given
        val presenter = PomodoroViewDialogPresenter(manager)

        // When
        presenter.onClickNegativeButton()

        // Then
        Mockito.verify(manager).stop()
    }
}
