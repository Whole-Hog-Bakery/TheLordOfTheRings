package org.middle.earth.lotr.data.local

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import io.ktor.client.call.body
import org.middle.earth.lotr.data.remote.TheOneApiHttpApi
import org.middle.earth.lotr.data.remote.dto.character.CharacterResponse
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class CharacterRemoteMediator(
    private val service: TheOneApiHttpApi,
    private val database: TheLordOfTheRingsDatabase
) : RemoteMediator<Int, CharacterDO>() {

    private val remoteKeyDAO = database.remoteKeyDAO()
    private val characterDAO = database.characterDAO()

    override suspend fun initialize(): InitializeAction {
        val remoteKey = remoteKeyDAO.select() ?: return InitializeAction.LAUNCH_INITIAL_REFRESH

        val cacheTimeout = TimeUnit.HOURS.convert(1, TimeUnit.MILLISECONDS)

        return if ((System.currentTimeMillis() - remoteKey.lastUpdated) >= cacheTimeout) InitializeAction.SKIP_INITIAL_REFRESH else InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, CharacterDO>): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> 1

                LoadType.PREPEND -> return MediatorResult.Success(true)

                LoadType.APPEND -> {
                    val remoteKey = remoteKeyDAO.select() ?: return MediatorResult.Success(true)

                    if (remoteKey.nextPage == null) return MediatorResult.Success(true)

                    remoteKey.nextPage
                }
            }

            val response: CharacterResponse = service.character(page = page).body()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) characterDAO.delete()

                val nextPage = if (response.characters.isEmpty()) null else page + 1

                val rows = response.characters.map { character ->
                    with(character) {
                        CharacterDO(
                            characterId = characterId,
                            height = height,
                            race = race,
                            gender = gender,
                            birth = birth,
                            spouse = spouse,
                            death = death,
                            realm = realm,
                            hair = hair,
                            name = name,
                            wikiUrl = wikiUrl
                        )
                    }
                }

                remoteKeyDAO.insert(RemoteKeyDO(nextPage = nextPage, lastUpdated = System.currentTimeMillis()))
                characterDAO.insert(rows)
            }

            MediatorResult.Success(endOfPaginationReached = response.characters.isEmpty())
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }
}