package fr.bowser.behaviortracker.showmode

import fr.bowser.behaviortracker.showmodeitem.ShowModeTimerViewContract
import fr.bowser.behaviortracker.showmodeitem.ShowModeTimerViewPresenter
import fr.bowser.behaviortracker.timer.TimeManager
import fr.bowser.behaviortracker.timer.Timer
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
    private lateinit var screen: ShowModeTimerViewContract.View

    @Mock
    private lateinit var timeManager: TimeManager

    private lateinit var presenter: ShowModeTimerViewPresenter

    @Before
    fun init() {
        presenter = ShowModeTimerViewPresenter(screen, timeManager)
    }

    @Test
    fun startRegisterToTimeManagerListener() {
        // When
        presenter.start()

        // Then
        Mockito.verify(timeManager).addListener(any())
    }

    @Test
    fun stopUnregisterToTimeManagerListener() {
        // When
        presenter.stop()

        // Then
        Mockito.verify(timeManager).removeListener(any())
    }

    @Test
    fun activeTimerSetViewToActiveState(){
        // Given
        val timer = Timer("timer", ColorUtils.COLOR_AMBER)
        timer.isActivate = true

        // When
        presenter.setTimer(timer)

        // Then
        Mockito.verify(screen).statusUpdated(true)
    }

    @Test
    fun clickOnViewWhenTimerIsDisableStartIt(){
        // Given
        val timer = Timer("timer", ColorUtils.COLOR_AMBER)
        timer.isActivate = false
        presenter.setTimer(timer)

        // When
        presenter.onClickView()

        // Then
        Mockito.verify(timeManager).startTimer(timer)
    }

    @Test
    fun clickOnViewWhenTimerIsEnableStopIt(){
        // Given
        val timer = Timer("timer", ColorUtils.COLOR_AMBER)
        timer.isActivate = true
        presenter.setTimer(timer)

        // When
        presenter.onClickView()

        // Then
        Mockito.verify(timeManager).stopTimer(timer)
    }

}