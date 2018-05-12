package fr.bowser.behaviortracker

import android.graphics.Color
import fr.bowser.behaviortracker.pomodoro.PomodoroContract
import fr.bowser.behaviortracker.pomodoro.PomodoroManager
import fr.bowser.behaviortracker.pomodoro.PomodoroPresenter
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.timer.TimerListManager
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PomodoroPresenterTest {

    @Mock
    private lateinit var view: PomodoroContract.View

    @Mock
    private lateinit var timerListManager: TimerListManager

    @Mock
    private lateinit var manager: PomodoroManager

    private lateinit var presenter: PomodoroPresenter

    private val timer0 = Timer(0L, 10L, "timer0", Color.RED, 0)

    @Before
    fun init(){
        presenter = PomodoroPresenter(view, manager, timerListManager)
    }

    @Test
    fun startPomodoroLogicWithoutPomodoroTimerStarted() {
        `when`(manager.isPomodoroStarted()).thenReturn(false)

        presenter.start()

        verify(view, times(1)).updatePomodoroTime(
                null,
                PomodoroPresenter.POMODORO_DURATION)
    }

    @Test
    fun startPomodoroLogicWithPomodoroTimerStarted(){
        `when`(manager.getPomodoroCurrentTimer()).thenReturn(timer0)
        `when`(manager.getPomodoroTime()).thenReturn(PomodoroPresenter.POMODORO_DURATION)
        `when`(manager.isPomodoroStarted()).thenReturn(true)

        presenter.start()

        verify(view, times(1)).updatePomodoroTime(timer0, PomodoroPresenter.POMODORO_DURATION)
    }

    @Test
    fun startPomodoroWithoutPomodoroTimerStartedRestoreView() {
        `when`(manager.isPomodoroRunning()).thenReturn(true)

        presenter.start()

        verify(view, times(1)).startCurrentAction()
    }

    @Test
    fun startPomodoroWithPomodoroTimerStartedRestoreView() {
        `when`(manager.isPomodoroRunning()).thenReturn(false)

        presenter.start()

        verify(view, times(1)).pauseCurrentAction()
    }

    @Test
    fun onChangePomodoroStatusStartPomodoroWhenNotStarted(){
        val timerList = ArrayList<Timer>()
        timerList.add(timer0)

        `when`(timerListManager.getTimerList()).thenReturn(timerList)

        presenter.onChangePomodoroStatus(0, 0)

        verify(manager, times(1)).startPomodoro(
                timer0,
                PomodoroPresenter.POMODORO_DURATION,
                timer0,
                PomodoroPresenter.REST_DURATION)
        verify(view, times(1)).startCurrentAction()
    }

    @Test
    fun onChangePomodoroStatusNotifyPauseWhenStarted(){
        val timerList = ArrayList<Timer>()
        timerList.add(timer0)

        `when`(timerListManager.getTimerList()).thenReturn(timerList)
        `when`(manager.isPomodoroStarted()).thenReturn(true)
        `when`(manager.isPomodoroRunning()).thenReturn(true)

        presenter.onChangePomodoroStatus(0, 0)

        verify(manager, times(1)).pause()
        verify(view, times(1)).pauseCurrentAction()
    }

    @Test
    fun onChangePomodoroStatusNotifyResumeWhenStarted(){
        val timerList = ArrayList<Timer>()
        timerList.add(timer0)

        `when`(timerListManager.getTimerList()).thenReturn(timerList)
        `when`(manager.isPomodoroStarted()).thenReturn(true)
        `when`(manager.isPomodoroRunning()).thenReturn(false)

        presenter.onChangePomodoroStatus(0, 0)

        verify(manager, times(1)).resume()
        verify(view, times(1)).startCurrentAction()
    }

}