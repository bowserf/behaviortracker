package fr.bowser.behaviortracker.timer

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "Timer")
data class Timer(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Long,
    @ColumnInfo(name = "current_time") val currentTime: Long,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "color") var color: Int,
    @ColumnInfo(name = "creation_date_ms") var creationDateTimestamp: Long,
    @ColumnInfo(name = "last_update_ms") var lastUpdateTimestamp: Long,
    @ColumnInfo(name = "position") var position: Int
) {

    @Ignore
    var isActivate: Boolean = false

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
        isActivate: Boolean = false
    ) : this(
        0,
        currentTime,
        name,
        color,
        creationDateTimestamp,
        lastUpdateTimestamp,
        position
    ) {
        this.isActivate = isActivate
    }

    override fun equals(other: Any?): Boolean {
        if (other is Timer) {
            if (other.id == id
                && other.color == color
                && other.isActivate == isActivate
                && other.currentTime == currentTime
                && other.name == name
                && other.time == time
                && other.creationDateTimestamp == creationDateTimestamp
                && other.lastUpdateTimestamp == lastUpdateTimestamp
                && other.position == position
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
        time = currentTime.toFloat()
    }
}