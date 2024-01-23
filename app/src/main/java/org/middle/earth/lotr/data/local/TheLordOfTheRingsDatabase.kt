package org.middle.earth.lotr.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import org.middle.earth.lotr.data.local.dao.CharacterDAO
import org.middle.earth.lotr.data.local.dao.QuoteDAO
import org.middle.earth.lotr.data.local.dao.RemoteKeyDAO

@Database(
    entities = [
        CharacterDO::class,
        QuoteDO::class,
        RemoteKeyDO::class,
    ],
    version = 1,
    exportSchema = false,
)

abstract class TheLordOfTheRingsDatabase : RoomDatabase() {
    abstract fun characterDAO(): CharacterDAO
    abstract fun quoteDAO(): QuoteDAO
    abstract fun remoteKeyDAO(): RemoteKeyDAO
}