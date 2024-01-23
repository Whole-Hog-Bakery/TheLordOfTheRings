package org.middle.earth.lotr.feature.character

import io.ktor.client.call.body
import org.middle.earth.lotr.data.remote.PingHttpApi
import org.middle.earth.lotr.data.remote.TheOneApiHttpApi
import org.middle.earth.lotr.data.remote.dto.character.CharacterResponse
import javax.inject.Inject

private const val TAG = "CharacterRepository"

class CharacterRepository @Inject constructor(
    private val pingService: PingHttpApi,
    private val theOneApiService: TheOneApiHttpApi
) {
    suspend fun characters(): CharacterResponse? {
        pingService.ping()

        return theOneApiService.character().getOrNull()?.body()

    }

}