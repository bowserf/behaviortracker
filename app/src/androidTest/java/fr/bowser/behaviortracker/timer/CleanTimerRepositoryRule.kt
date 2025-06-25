package fr.bowser.behaviortracker.timer

import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class CleanTimerRepositoryRule : TestRule {

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                before()
                try {
                    base.evaluate()
                } finally {
                    after()
                }
            }
        }
    }

    private fun before() {
        TimerRepositoryClean.removeAllTimers()
    }

    private fun after() {
        TimerRepositoryClean.removeAllTimers()
    }
}
