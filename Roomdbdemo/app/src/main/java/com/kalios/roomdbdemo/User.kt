package com.kalios.roomdbdemo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "userTable")
data class User(
    @ColumnInfo(name = "first_name") val firstName: String?,
    @ColumnInfo(name = "last_name") val lastName: String?,
    @ColumnInfo(name = "profile") val profile: ByteArray?
){
    @PrimaryKey(autoGenerate = true) var id = 0
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (profile != null) {
            if (other.profile == null) return false
            if (!profile.contentEquals(other.profile)) return false
        } else if (other.profile != null) return false

        return true
    }

    override fun hashCode(): Int {
        return profile?.contentHashCode() ?: 0
    }
}