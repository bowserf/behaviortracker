package fr.bowser.behaviortracker.crash_record

import android.app.ActivityManager
import android.app.ApplicationExitInfo
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class CrashRecordManagerImpl(
    context: Context,
    private val preferences: SharedPreferences,
    coroutineContext: CoroutineContext = Dispatchers.IO,
) : CrashRecordManager {

    private val coroutineScope = CoroutineScope(coroutineContext)

    private val activityManager =
        ContextCompat.getSystemService(context, ActivityManager::class.java)!!

    private var unprocessedCrashRecord: CrashRecordManager.CrashRecord? = null

    override fun initialize() {
        coroutineScope.launch {
            retrieveUnprocessedExitReason()
        }
    }

    override fun getUnprocessedCrashRecord(): CrashRecordManager.CrashRecord? {
        return unprocessedCrashRecord
    }

    private suspend fun retrieveUnprocessedExitReason() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            return
        }

        withContext(Dispatchers.IO) {
            val lastCrashRecordTimestamp =
                preferences.getLong(PREFERENCE_KEY_CRASH_RECORD_LAST_TIMESTAMP, 0)
            val processExitReasons = activityManager.getHistoricalProcessExitReasons(null, 0, 0)
            val unprocessedExitReasons = mutableListOf<ApplicationExitInfo>()
            for (reason in processExitReasons) {
                if (isExistReasonACrash(reason) && reason.timestamp > lastCrashRecordTimestamp) {
                    unprocessedExitReasons.add(reason)
                    break
                }
            }
            if (unprocessedExitReasons.size > 0) {
                val unprocessedExitReason = unprocessedExitReasons.single()
                preferences.edit {
                    putLong(
                        PREFERENCE_KEY_CRASH_RECORD_LAST_TIMESTAMP,
                        unprocessedExitReason.timestamp
                    )
                }
                unprocessedCrashRecord = CrashRecordManager.CrashRecord(
                    unprocessedExitReason.reason,
                    unprocessedExitReason.timestamp,
                )
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.R)
    private fun isExistReasonACrash(reason: ApplicationExitInfo) =
        reason.reason == ApplicationExitInfo.REASON_CRASH ||
            reason.reason == ApplicationExitInfo.REASON_ANR ||
            reason.reason == ApplicationExitInfo.REASON_CRASH_NATIVE ||
            reason.reason == ApplicationExitInfo.REASON_EXCESSIVE_RESOURCE_USAGE ||
            reason.reason == ApplicationExitInfo.REASON_LOW_MEMORY

    companion object {
        const val PREFERENCES_NAME = "crash_record.pref"
        private const val PREFERENCE_KEY_CRASH_RECORD_LAST_TIMESTAMP =
            "preference_key.crash_record.last_timestamp"
    }
}
