package org.middle.earth.lotr.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import org.middle.earth.lotr.data.local.CharacterDO

@Dao
interface CharacterDAO : BaseDAO<CharacterDO> {

    @Transaction
    @Query("SELECT * FROM character_table ORDER BY name COLLATE NOCASE ASC")
    suspend fun fetch(): List<CharacterDO>

    @Transaction
    @Query("SELECT * FROM character_table ORDER BY name COLLATE NOCASE ASC")
    fun pagingSource(): PagingSource<Int, CharacterDO>

    @Transaction
    @Query("DELETE FROM character_table")
    suspend fun delete()
}