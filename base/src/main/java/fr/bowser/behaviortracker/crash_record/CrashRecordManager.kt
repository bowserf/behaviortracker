package fr.bowser.behaviortracker.crash_record

interface CrashRecordManager {

    fun initialize()

    fun getUnprocessedCrashRecord(): CrashRecord?

    data class CrashRecord(
        val reason: Int,
        val timestamp: Long,
    )
}
