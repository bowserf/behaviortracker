package fr.bowser.behaviortracker.timer

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "Timer")
data class Timer(@PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Long,
                 @ColumnInfo(name = "current_time") val currentTime: Long,
                 @ColumnInfo(name = "name") var name: String,
                 @ColumnInfo(name = "color") var color: Int,
                 @ColumnInfo(name = "position") var position: Int) {

    @Ignore
    var isActivate: Boolean = false

    @Ignore
    var time: Float = 0f

    @Ignore
    constructor(name: String, color: Int, isActivate: Boolean = false, position: Int = 0)
            : this(0, 0, name, color, position) {
        this.isActivate = isActivate
    }

    init {
        time = currentTime.toFloat()
    }

}