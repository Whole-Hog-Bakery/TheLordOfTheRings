package org.middle.earth.lotr.feature.character

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.middle.earth.lotr.data.local.CharacterDO
import org.middle.earth.lotr.data.local.CharacterRemoteMediator
import org.middle.earth.lotr.data.local.MiddleEarthRepository
import org.middle.earth.lotr.data.remote.PAGE_SIZE
import javax.inject.Inject

private const val TAG = "CharacterRepository"

class CharacterRepository @Inject constructor(
    private val repository: MiddleEarthRepository,
) {
    @OptIn(ExperimentalPagingApi::class)
    fun characters(): Flow<PagingData<CharacterDO>> {
        val dbSource = {
            repository.database.characterDAO().pagingSource()
        }
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false, prefetchDistance = 59, initialLoadSize = 197),
            remoteMediator = CharacterRemoteMediator(repository),
            pagingSourceFactory = dbSource
        ).flow
    }

}