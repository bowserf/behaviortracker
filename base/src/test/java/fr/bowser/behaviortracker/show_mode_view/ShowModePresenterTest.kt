package fr.bowser.behaviortracker.show_mode_view

import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.timer_repository.TimerRepository
import fr.bowser.behaviortracker.utils.ColorUtils
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ShowModePresenterTest {

    @Mock
    private lateinit var screen: ShowModeViewContract.Screen

    @Mock
    private lateinit var timerRepository: TimerRepository

    @Test
    fun startSelectTheGoodTimer() {
        // Given
        val presenter = ShowModeViewPresenter(screen, timerRepository)
        val timerList = mutableListOf<Timer>()
        timerList.add(Timer(9, 0, "timer1", ColorUtils.COLOR_AMBER, 0, 0, 0, false))
        timerList.add(Timer(10, 0, "timer2", ColorUtils.COLOR_AMBER, 0, 0, 1, false))
        timerList.add(Timer(11, 0, "timer3", ColorUtils.COLOR_AMBER, 0, 0, 2, false))
        Mockito.`when`(timerRepository.getTimerList()).thenReturn(timerList)

        // When
        presenter.onStart(10)

        // Then
        Mockito.verify(screen).displayTimerList(timerList, 1)
    }

    @Test
    fun onClickScreeOff() {
        // Given
        val presenter = ShowModeViewPresenter(screen, timerRepository)

        // When
        presenter.onClickScreeOff()

        // Then
        Mockito.verify(screen).keepScreeOn(false)
        Assert.assertFalse(presenter.keepScreenOn())
    }

    @Test
    fun onClickScreeOn() {
        // Given
        val presenter = ShowModeViewPresenter(screen, timerRepository)

        // When
        presenter.onClickScreeOn()

        // Then
        Mockito.verify(screen).keepScreeOn(true)
        Assert.assertTrue(presenter.keepScreenOn())
    }
}
