package fr.bowser.behaviortracker.alarm

import fr.bowser.behaviortracker.alarm.AlarmDialogPresenter
import fr.bowser.behaviortracker.alarm.AlarmTime
import fr.bowser.behaviortracker.alarm.AlarmTimerContract
import fr.bowser.behaviortracker.alarm.AlarmTimerManager
import fr.bowser.behaviortracker.event.EventManager
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AlarmDialogPresenterTest {

    @Mock
    private lateinit var alarmTimerManager: AlarmTimerManager

    @Mock
    private lateinit var view: AlarmTimerContract.View

    @Mock
    private lateinit var eventManager: EventManager

    private lateinit var presenter: AlarmDialogPresenter

    @Before
    fun initPresenter() {
        presenter = AlarmDialogPresenter(view, alarmTimerManager, eventManager)
    }

    @Test
    fun restoreStatusAfterViewDisplayed() {
        val alarmTime = AlarmTime(0, 0, true)
        Mockito.`when`(alarmTimerManager.getSavedAlarmTimer()).thenReturn(alarmTime)

        // When
        presenter.start()

        // Then
        Mockito.verify(view, times(1)).restoreAlarmStatus(alarmTime)
    }

    @Test
    fun onClickValidateWithEnabledAlarm() {
        // Given
        val alarmTime = AlarmTime(0, 0, true)

        // when
        presenter.onClickValidate(alarmTime)

        // Then
        Mockito.verify(view, times(1)).displayMessageAlarmEnabled()
    }

    @Test
    fun onClickValidateWithDisabledAlarm() {
        // Given
        val alarmTime = AlarmTime(0, 0, false)

        // when
        presenter.onClickValidate(alarmTime)

        // Then
        Mockito.verify(view, times(1)).displayMessageAlarmDisabled()
    }

}