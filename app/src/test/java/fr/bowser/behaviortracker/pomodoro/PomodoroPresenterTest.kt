package fr.bowser.behaviortracker.pomodoro

import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.utils.ColorUtils
import fr.bowser.behaviortracker.utils.MockitoUtils.any
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PomodoroPresenterTest {

    @Mock
    private lateinit var screen: PomodoroContract.Screen

    @Mock
    private lateinit var manager: PomodoroManager

    @Test
    fun restoreCurrentTimerIfPomodoroIsRunning() {
        // Given
        val presenter = PomodoroPresenter(screen, manager, listOf())
        val timer = Timer("name", ColorUtils.COLOR_AMBER, false, 0)
        Mockito.`when`(manager.currentTimer).thenReturn(timer)

        // When
        presenter.start()

        // Then
        Mockito.verify(screen).updatePomodoroTimer(any(), Mockito.anyLong(), Mockito.anyLong())
    }

    @Test
    fun displayEmptyViewWhenStartAndNoSessionIsActivte() {
        // Given
        val presenter = PomodoroPresenter(screen, manager, listOf())

        // When
        presenter.start()

        // Then
        Mockito.verify(screen).displayEmptyView()
    }

    @Test
    fun displayPomodoroDialogIfAStepIfFinishedAndUserGoBackToTheView() {
        // Given
        val presenter = PomodoroPresenter(screen, manager, listOf())
        Mockito.`when`(manager.isPendingState).thenReturn(true)

        // When
        presenter.start()

        // Then
        Mockito.verify(screen).displayPomodoroDialog()
    }

    @Test
    fun onClickFabDisplayChoosePomodoroTimer() {
        // Given
        val timer = Timer("name", ColorUtils.COLOR_AMBER, false, 0)
        val presenter = PomodoroPresenter(screen, manager, listOf(timer))
        Mockito.`when`(manager.isStarted).thenReturn(false)

        // When
        presenter.onClickFab()

        // Then
        Mockito.verify(screen).displayChoosePomodoroTimer()
    }

    @Test
    fun onClickFabDisplayNoTimerAvailable() {
        // Given
        val presenter = PomodoroPresenter(screen, manager, listOf())
        Mockito.`when`(manager.isStarted).thenReturn(false)

        // When
        presenter.onClickFab()

        // Then
        Mockito.verify(screen).displayNoTimerAvailable()
    }

    @Test
    fun onClickFabPausePomodoro() {
        // Given
        val presenter = PomodoroPresenter(screen, manager, listOf())
        Mockito.`when`(manager.isStarted).thenReturn(true)
        Mockito.`when`(manager.isRunning).thenReturn(true)

        // When
        presenter.onClickFab()

        // Then
        Mockito.verify(manager).pause()
    }

    @Test
    fun onClickFabResumePomodoro() {
        // Given
        val presenter = PomodoroPresenter(screen, manager, listOf())
        Mockito.`when`(manager.isStarted).thenReturn(true)
        Mockito.`when`(manager.isRunning).thenReturn(false)

        // When
        presenter.onClickFab()

        // Then
        Mockito.verify(manager).resume()
    }

    @Test
    fun onClickStopPomodoro() {
        // Given
        val presenter = PomodoroPresenter(screen, manager, listOf())

        // When
        presenter.onClickStopPomodoro()

        // Then
        Mockito.verify(manager).stop()
        Mockito.verify(screen).displayEmptyView()
    }

    @Test
    fun onClickCreateTimerDisplayDialog() {
        // Given
        val presenter = PomodoroPresenter(screen, manager, listOf())

        // When
        presenter.onClickCreateTimer()

        // Then
        Mockito.verify(screen).displayCreateTimer()
    }
}