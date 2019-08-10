package fr.bowser.behaviortracker.pomodoro

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
        val presenter = PomodoroDialogPresenter(manager)

        // When
        presenter.onClickPositionButton()

        // Then
        Mockito.verify(manager).resume()
    }

    @Test
    fun clickOnNegativeButtonStopSession() {
        // Given
        val presenter = PomodoroDialogPresenter(manager)

        // When
        presenter.onClickNegativeButton()

        // Then
        Mockito.verify(manager).stop()
    }
}