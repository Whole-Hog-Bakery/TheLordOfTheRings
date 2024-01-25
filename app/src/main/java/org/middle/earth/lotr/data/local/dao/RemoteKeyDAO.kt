package org.middle.earth.lotr.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import org.middle.earth.lotr.data.local.MIDDLE_EARTH
import org.middle.earth.lotr.data.local.RemoteKeyDO

@Dao
interface RemoteKeyDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(keys: List<RemoteKeyDO>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(key: RemoteKeyDO)

    @Transaction
    @Query("SELECT * FROM lotr_remote_key_table WHERE id = :key")
    suspend fun select(key: String = MIDDLE_EARTH): RemoteKeyDO?

    @Query("DELETE FROM lotr_remote_key_table WHERE id = :key")
    suspend fun clearKeys(key: String = MIDDLE_EARTH)
}