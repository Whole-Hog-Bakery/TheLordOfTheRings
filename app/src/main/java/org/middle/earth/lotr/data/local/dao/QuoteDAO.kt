package org.middle.earth.lotr.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import org.middle.earth.lotr.data.local.QuoteDO

@Dao
interface QuoteDAO : BaseDAO<QuoteDO> {

    @Transaction
    @Query("SELECT * FROM quote_table WHERE character_id = :characterId ORDER BY dialog")
    suspend fun fetch(characterId: String): List<QuoteDO>

    @Transaction
    @Query("DELETE FROM quote_table")
    suspend fun delete()
}