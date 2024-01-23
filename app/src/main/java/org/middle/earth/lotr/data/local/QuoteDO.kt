package org.middle.earth.lotr.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "quote_table",
    indices = [
        Index(value = ["movie_id"], unique = false),
        Index(value = ["character_id"], unique = false),
    ]
)
data class QuoteDO(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "quote_id") val quoteId: String,
    @ColumnInfo(name = "dialog") val dialog: String,
    @ColumnInfo(name = "movie_id") val movieId: String,
    @ColumnInfo(name = "character_id") val characterId: String,
    @ColumnInfo(name = "id") val duplicateQuoteId: String
)