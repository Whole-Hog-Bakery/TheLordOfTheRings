package org.middle.earth.lotr.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "character_table"
)
data class CharacterDO(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "character_id") val characterId: String,
    @ColumnInfo(name = "height") val height: String,
    @ColumnInfo(name = "race") val race: String,
    @ColumnInfo(name = "gender") val gender: String?,
    @ColumnInfo(name = "birth") val birth: String,
    @ColumnInfo(name = "spouse") val spouse: String,
    @ColumnInfo(name = "death") val death: String,
    @ColumnInfo(name = "realm") val realm: String,
    @ColumnInfo(name = "hair") val hair: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "wikiUrl") val wikiUrl: String?
)