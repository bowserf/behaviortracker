package fr.bowser.behaviortracker.timer_service

import fr.bowser.behaviortracker.instantapp.InstantAppManager
import fr.bowser.behaviortracker.pomodoro.PomodoroManager
import fr.bowser.behaviortracker.timer.Timer
import fr.bowser.behaviortracker.timer.TimerManager
import fr.bowser.behaviortracker.timer_repository.TimerRepository
import fr.bowser.behaviortracker.utils.TimeConverter

class TimerServicePresenter(
    private val screen: TimerServiceContract.Screen,
    private val instantAppManager: InstantAppManager,
    private val timeManager: TimerManager,
    private val timerRepository: TimerRepository,
    private val pomodoroManager: PomodoroManager,
    private val addOn: AddOn
) : TimerServiceContract.Presenter {

    private var isNotificationDisplayed = false

    private val pomodoroListener = createPomodoroListener()

    private val timeManagerListener = createTimeManagerListener()

    private val timerRepositoryListener = createTimerRepositoryListener()

    private var timer: Timer? = null

    override fun attach() {
        timeManager.addListener(timeManagerListener)
        timerRepository.addListener(timerRepositoryListener)
        pomodoroManager.addListener(pomodoroListener)
    }

    override fun detach() {
        timeManager.removeListener(timeManagerListener)
        timerRepository.removeListener(timerRepositoryListener)
        pomodoroManager.removeListener(pomodoroListener)
    }

    override fun dismissNotification() {
        if (!isNotificationDisplayed) {
            return
        }

        timer = null
        isNotificationDisplayed = false

        screen.dismissNotification()
    }

    override fun start() {
        if (pomodoroManager.isStarted) {
            val timer = if (pomodoroManager.isBreakStep()) {
                pomodoroManager.getBreakTimer()
            } else {
                pomodoroManager.currentTimer!!
            }
            if (timer.isActivate) {
                displayTimerNotification(timer)
            } else {
                pauseTimerNotif(timer)
            }
            this.timer = timer
        } else {
            val timer = timerRepository.getTimerList().maxByOrNull { it.lastUpdateTimestamp }!!
            if (timer.isActivate) {
                displayTimerNotification(timer)
            } else {
                pauseTimerNotif(timer)
            }
            this.timer = timer
        }
    }

    private fun displayTimerNotification(modifiedTimer: Timer) {
        if (timer == modifiedTimer) {
            resumeTimerNotif(modifiedTimer)
        } else {
            this.timer = modifiedTimer

            isNotificationDisplayed = true

            screen.displayTimerNotification(
                getNotificationTitle(modifiedTimer),
                getNotificationMessage(modifiedTimer)
            )
        }
    }

    private fun resumeTimerNotif(modifiedTimer: Timer) {
        if (!isNotificationDisplayed || timer != modifiedTimer) {
            return
        }

        screen.resumeTimerNotification(getNotificationTitle(modifiedTimer))
    }

    private fun renameTimerNotif(updatedTimer: Timer) {
        if (timer != updatedTimer) {
            return
        }

        screen.renameTimerNotification(updatedTimer.name)
    }

    private fun pauseTimerNotif(modifiedTimer: Timer) {
        if (instantAppManager.isInstantApp()) {
            dismissNotification()
        }
        if (!isNotificationDisplayed || timer != modifiedTimer) {
            return
        }

        screen.pauseTimerNotification()
    }

    private fun continuePomodoroNotif() {
        screen.continuePomodoroNotification()
    }

    private fun updateTimeNotif() {
        screen.updateTimeNotification(getNotificationMessage(timer!!))
    }

    private fun getNotificationTitle(timer: Timer): String {
        return if (pomodoroManager.isStarted) {
            addOn.getNotificationName(timer.name)
        } else {
            timer.name
        }
    }

    private fun getNotificationMessage(timer: Timer): String {
        return if (pomodoroManager.isStarted) {
            TimeConverter.convertSecondsToHumanTime(
                pomodoroManager.pomodoroTime,
                TimeConverter.DisplayHoursMode.Never
            )
        } else {
            TimeConverter.convertSecondsToHumanTime(timer.time.toLong())
        }
    }

    private fun createTimerRepositoryListener(): TimerRepository.Listener {
        return object : TimerRepository.Listener {
            override fun onTimerAdded(updatedTimer: Timer) {
                if (pomodoroManager.isStarted) {
                    return
                }
                // if timer is directly activate, display it in the notification
                if (updatedTimer.isActivate) {
                    displayTimerNotification(updatedTimer)
                }
            }

            override fun onTimerRemoved(removedTimer: Timer) {
                if (timer != removedTimer) {
                    return
                }
                dismissNotification()
            }

            override fun onTimerRenamed(updatedTimer: Timer) {
                if (timer != updatedTimer) {
                    return
                }
                renameTimerNotif(updatedTimer)
            }
        }
    }

    private fun createTimeManagerListener(): TimerManager.Listener {
        return object : TimerManager.Listener {
            override fun onTimerStateChanged(updatedTimer: Timer) {
                if (pomodoroManager.isStarted) {
                    return
                }

                if (updatedTimer.isActivate) {
                    displayTimerNotification(updatedTimer)
                } else {
                    pauseTimerNotif(updatedTimer)
                }
            }

            override fun onTimerTimeChanged(updatedTimer: Timer) {
                if (pomodoroManager.isStarted) {
                    return
                }

                if (timer == updatedTimer) {
                    updateTimeNotif()
                }
            }
        }
    }

    private fun createPomodoroListener(): PomodoroManager.Listener {
        return object : PomodoroManager.Listener {

            override fun onPomodoroSessionStarted(newTimer: Timer, duration: Long) {
                displayTimerNotification(newTimer)
            }

            override fun onPomodoroSessionStop() {
                dismissNotification()
            }

            override fun onTimerStateChanged(updatedTimer: Timer) {
                if (updatedTimer.isActivate) {
                    displayTimerNotification(updatedTimer)
                } else {
                    pauseTimerNotif(updatedTimer)
                }
            }

            override fun updateTime(updatedTimer: Timer, currentTime: Long) {
                if (timer != updatedTimer) {
                    return
                }
                updateTimeNotif()
            }

            override fun onCountFinished(newTimer: Timer, duration: Long) {
                continuePomodoroNotif()
            }
        }
    }

    interface AddOn {
        fun getNotificationName(timerName: String): String
    }
}
