package fr.bowser.behaviortracker.show_mode_view

import fr.bowser.behaviortracker.show_mode_item_view.ShowModeItemViewContract
import fr.bowser.behaviortracker.show_mode_item_view.ShowModeItemViewPresenter
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.timer.TimerManager
import fr.bowser.behaviortracker.utils.ColorUtils
import fr.bowser.behaviortracker.utils.MockitoUtils.any
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ShowModeTimerViewPresenterTest {

    @Mock
    private lateinit var screen: ShowModeItemViewContract.Screen

    @Mock
    private lateinit var timeManager: TimerManager

    private lateinit var presenter: ShowModeItemViewPresenter

    @Before
    fun init() {
        presenter = ShowModeItemViewPresenter(screen, timeManager)
    }

    @Test
    fun startRegisterToTimeManagerListener() {
        // When
        presenter.onStart()

        // Then
        Mockito.verify(timeManager).addListener(any())
    }

    @Test
    fun stopUnregisterToTimeManagerListener() {
        // When
        presenter.onStop()

        // Then
        Mockito.verify(timeManager).removeListener(any())
    }

    @Test
    fun activeTimerSetViewToActiveState() {
        // Given
        val timer = Timer("", ColorUtils.COLOR_AMBER)
        Mockito.`when`(timeManager.isRunning(timer)).thenReturn(true)

        // When
        presenter.setTimer(timer)

        // Then
        Mockito.verify(screen).statusUpdated(true)
    }

    @Test
    fun clickOnViewWhenTimerIsDisableStartIt() {
        // Given
        val timer = Timer("", ColorUtils.COLOR_AMBER)
        presenter.setTimer(timer)

        // When
        presenter.onClickView()

        // Then
        Mockito.verify(timeManager).startTimer(timer)
    }

    @Test
    fun clickOnViewWhenTimerIsEnableStopIt() {
        // Given
        val timer = Timer("", ColorUtils.COLOR_AMBER)
        Mockito.`when`(timeManager.isRunning(timer)).thenReturn(true)
        presenter.setTimer(timer)

        // When
        presenter.onClickView()

        // Then
        Mockito.verify(timeManager).stopTimer()
    }
}
