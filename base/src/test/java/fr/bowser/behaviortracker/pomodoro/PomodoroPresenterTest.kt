package fr.bowser.behaviortracker.pomodoro

import fr.bowser.behaviortracker.pomodoro_view.PomodoroViewContract
import fr.bowser.behaviortracker.pomodoro_view.PomodoroViewPresenter
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.timer_repository.TimerRepository
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
    private lateinit var screen: PomodoroViewContract.Screen

    @Mock
    private lateinit var doNotDisturbManager: DoNotDisturbManager

    @Mock
    private lateinit var manager: PomodoroManager

    @Mock
    private lateinit var timerRepository: TimerRepository

    @Test
    fun restoreCurrentTimerIfPomodoroIsRunning() {
        // Given
        val presenter = PomodoroViewPresenter(
            screen,
            manager,
            doNotDisturbManager,
            timerRepository,
            false
        )
        val timer = Timer("name", ColorUtils.COLOR_AMBER)
        val pomodoroConfiguration = PomodoroViewContract.Configuration(false)
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
        val presenter = PomodoroViewPresenter(
            screen,
            manager,
            doNotDisturbManager,
            timerRepository,
            false
        )
        val pomodoroConfiguration = PomodoroViewContract.Configuration(false)
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
        val presenter = PomodoroViewPresenter(
            screen,
            manager,
            doNotDisturbManager,
            timerRepository,
            false
        )
        val pomodoroConfiguration = PomodoroViewContract.Configuration(false)
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
        val presenter = PomodoroViewPresenter(
            screen,
            manager,
            doNotDisturbManager,
            timerRepository,
            false
        )
        Mockito.`when`(manager.isStarted).thenReturn(false)
        Mockito.`when`(timerRepository.getTimerList()).thenReturn(listOf(timer))

        // When
        presenter.onClickStartSession()

        // Then
        Mockito.verify(screen).displayChoosePomodoroTimer()
    }

    @Test
    fun onClickFabDisplayNoTimerAvailable() {
        // Given
        val presenter = PomodoroViewPresenter(
            screen,
            manager,
            doNotDisturbManager,
            timerRepository,
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
        val presenter = PomodoroViewPresenter(
            screen,
            manager,
            doNotDisturbManager,
            timerRepository,
            false
        )
        Mockito.`when`(doNotDisturbManager.getDnDState())
            .thenReturn(DoNotDisturbManager.DnDState.ALL)
        val pomodoroConfiguration = PomodoroViewContract.Configuration(false)
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
        val presenter = PomodoroViewPresenter(
            screen,
            manager,
            doNotDisturbManager,
            timerRepository,
            false
        )

        // When
        presenter.onClickCreateTimer()

        // Then
        Mockito.verify(screen).displayCreateTimerScreen()
    }
}
