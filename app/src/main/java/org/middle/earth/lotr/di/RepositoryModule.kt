package org.middle.earth.lotr.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.middle.earth.lotr.data.remote.PingHttpApi
import org.middle.earth.lotr.data.remote.TheOneApiHttpApi
import org.middle.earth.lotr.feature.character.CharacterRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideCharacterRepository(pingService: PingHttpApi, theOneApiService: TheOneApiHttpApi): CharacterRepository {
        return CharacterRepository(pingService, theOneApiService)
    }

}