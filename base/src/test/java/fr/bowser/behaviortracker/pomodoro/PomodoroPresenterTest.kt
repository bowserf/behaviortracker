package fr.bowser.behaviortracker.pomodoro

import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.timer.TimerListManager
import fr.bowser.behaviortracker.utils.ColorUtils
import fr.bowser.behaviortracker.utils.MockitoUtils.any
import fr.bowser.feature_do_not_disturb.DoNotDisturbManager
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
    private lateinit var doNotDisturbManager: DoNotDisturbManager

    @Mock
    private lateinit var manager: PomodoroManager

    @Mock
    private lateinit var timerListManager: TimerListManager

    @Test
    fun restoreCurrentTimerIfPomodoroIsRunning() {
        // Given
        val presenter = PomodoroPresenter(
            screen,
            manager,
            doNotDisturbManager,
            timerListManager,
            false
        )
        val timer = Timer("name", ColorUtils.COLOR_AMBER)
        val pomodoroConfiguration = PomodoroContract.Configuration(false)
        Mockito.`when`(manager.currentTimer).thenReturn(timer)
        Mockito.`when`(doNotDisturbManager.getDnDState())
            .thenReturn(DoNotDisturbManager.DnDState.ALL)

        // When
        presenter.onStart(pomodoroConfiguration)

        // Then
        Mockito.verify(screen).updatePomodoroTimer(any(), Mockito.anyLong(), Mockito.anyLong())
    }

    @Test
    fun displayEmptyViewWhenStartAndNoSessionIsActive() {
        // Given
        val presenter = PomodoroPresenter(
            screen,
            manager,
            doNotDisturbManager,
            timerListManager,
            false
        )
        val pomodoroConfiguration = PomodoroContract.Configuration(false)
        Mockito.`when`(doNotDisturbManager.getDnDState())
            .thenReturn(DoNotDisturbManager.DnDState.ALL)

        // When
        presenter.onStart(pomodoroConfiguration)

        // Then
        Mockito.verify(screen).displayEmptyView()
    }

    @Test
    fun displayPomodoroDialogIfAStepIfFinishedAndUserGoBackToTheView() {
        // Given
        val presenter = PomodoroPresenter(
            screen,
            manager,
            doNotDisturbManager,
            timerListManager,
            false
        )
        val pomodoroConfiguration = PomodoroContract.Configuration(false)
        Mockito.`when`(manager.isPendingState).thenReturn(true)
        Mockito.`when`(doNotDisturbManager.getDnDState())
            .thenReturn(DoNotDisturbManager.DnDState.ALL)

        // When
        presenter.onStart(pomodoroConfiguration)

        // Then
        Mockito.verify(screen).displayPomodoroDialog()
    }

    @Test
    fun onClickFabDisplayChoosePomodoroTimer() {
        // Given
        val timer = Timer("name", ColorUtils.COLOR_AMBER)
        val presenter = PomodoroPresenter(
            screen,
            manager,
            doNotDisturbManager,
            timerListManager,
            false
        )
        Mockito.`when`(manager.isStarted).thenReturn(false)
        Mockito.`when`(timerListManager.getTimerList()).thenReturn(listOf(timer))

        // When
        presenter.onClickStartSession()

        // Then
        Mockito.verify(screen).displayChoosePomodoroTimer()
    }

    @Test
    fun onClickFabDisplayNoTimerAvailable() {
        // Given
        val presenter = PomodoroPresenter(
            screen,
            manager,
            doNotDisturbManager,
            timerListManager,
            false
        )
        Mockito.`when`(manager.isStarted).thenReturn(false)

        // When
        presenter.onClickStartSession()

        // Then
        Mockito.verify(screen).displayNoTimerAvailable()
    }

    @Test
    fun onClickStopPomodoro() {
        // Given
        val presenter = PomodoroPresenter(
            screen,
            manager,
            doNotDisturbManager,
            timerListManager,
            false
        )
        Mockito.`when`(doNotDisturbManager.getDnDState())
            .thenReturn(DoNotDisturbManager.DnDState.ALL)
        val pomodoroConfiguration = PomodoroContract.Configuration(false)
        presenter.onStart(pomodoroConfiguration)

        // When
        presenter.onClickStopPomodoro()

        // Then
        Mockito.verify(manager).stop()
        Mockito.verify(screen).displayEmptyView()
    }

    @Test
    fun onClickCreateTimerDisplayDialog() {
        // Given
        val presenter = PomodoroPresenter(
            screen,
            manager,
            doNotDisturbManager,
            timerListManager,
            false
        )

        // When
        presenter.onClickCreateTimer()

        // Then
        Mockito.verify(screen).displayCreateTimerScreen()
    }
}
