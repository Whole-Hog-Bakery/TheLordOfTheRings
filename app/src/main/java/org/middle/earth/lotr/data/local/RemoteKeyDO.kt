package org.middle.earth.lotr.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val MIDDLE_EARTH = "MIDDLE_EARTH"

@Entity(
    tableName = "lotr_remote_key_table"
)
data class RemoteKeyDO(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id") val id: String = MIDDLE_EARTH,
    @ColumnInfo(name = "next_page") val nextPage: Int?,
    @ColumnInfo(name = "last_updated") val lastUpdated: Long
)