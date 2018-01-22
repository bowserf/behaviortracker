package fr.bowser.behaviortracker.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "Timer" )
data class Timer(@PrimaryKey @ColumnInfo(name = "id") var id: Long,
                 @ColumnInfo(name = "current_time") var currentTime: Long,
                 @ColumnInfo(name = "name") val name: String,
                 @ColumnInfo(name = "color") var color: Int)