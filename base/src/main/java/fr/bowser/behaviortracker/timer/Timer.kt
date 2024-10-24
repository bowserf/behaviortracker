package fr.bowser.behaviortracker.timer

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "Timer")
data class Timer(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long,
    @ColumnInfo(name = "current_time") val currentTimeInSecond: Long,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "color") var colorId: Int,
    @ColumnInfo(name = "creation_date_ms") var creationDateTimestamp: Long,
    @ColumnInfo(name = "last_update_ms") var lastUpdateTimestamp: Long,
    @ColumnInfo(name = "position") var position: Int,
    @ColumnInfo(name = "is_finished") var isFinished: Boolean,
) {

    @Ignore
    var time: Float = 0f

    @Ignore
    constructor(
        name: String,
        color: Int,
        currentTime: Long = 0,
        creationDateTimestamp: Long = 0,
        lastUpdateTimestamp: Long = 0,
        position: Int = 0,
        isFinished: Boolean = false,
    ) : this(
        0,
        currentTime,
        name,
        color,
        creationDateTimestamp,
        lastUpdateTimestamp,
        position,
        isFinished,
    )

    override fun equals(other: Any?): Boolean {
        if (other is Timer) {
            if (other.id == id &&
                other.colorId == colorId &&
                other.currentTimeInSecond == currentTimeInSecond &&
                other.name == name &&
                other.time == time &&
                other.creationDateTimestamp == creationDateTimestamp &&
                other.lastUpdateTimestamp == lastUpdateTimestamp &&
                other.position == position &&
                other.isFinished == isFinished
            ) {
                return true
            }
        }
        return false
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    init {
        time = currentTimeInSecond.toFloat()
    }
}
