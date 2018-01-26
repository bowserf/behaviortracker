package fr.bowser.behaviortracker.timer

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "Timer" )
data class Timer(@PrimaryKey @ColumnInfo(name = "id") var id: Long,
                 @ColumnInfo(name = "current_time") var currentTime: Long,
                 @ColumnInfo(name = "name") val name: String,
                 @ColumnInfo(name = "color") var color: Int){
    @Ignore
    constructor(name: String, color: Int)
            : this(0, 0, name, color)
}