package org.middle.earth.lotr.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.requery.android.database.sqlite.RequerySQLiteOpenHelperFactory
import org.middle.earth.lotr.BuildConfig.DEBUG
import org.middle.earth.lotr.data.local.TheLordOfTheRingsDatabase
import javax.inject.Singleton

private const val DATABASE_NAME = "zot-hero-database"

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun providesRoomDatabaseCallback(): RoomDatabase.Callback {
        return object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // When temp_store is MEMORY (2) temporary tables and indices are kept as if they were in pure in-memory databases.
                db.query("PRAGMA temp_store=2;")
                db.query("PRAGMA main.journal_mode=memory;")
                db.query("PRAGMA main.auto_vacuum=incremental;")
                db.query("PRAGMA main.cache_size=-64000;")
            }

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                db.query("PRAGMA main.synchronous=normal;")
                db.query("PRAGMA main.mmap_size=30000000000;")
                db.query("PRAGMA main.page_size=32768;")
            }
        }
    }

    @Provides
    @Singleton
    fun providesTheLordOfTheRingsDatabase(
        @ApplicationContext appContext: Context,
        callback: RoomDatabase.Callback
    ): TheLordOfTheRingsDatabase {
        return if (DEBUG) Room
            .databaseBuilder(appContext, TheLordOfTheRingsDatabase::class.java, DATABASE_NAME)
            .addCallback(callback)
            .build()
        else Room
            .databaseBuilder(appContext, TheLordOfTheRingsDatabase::class.java, DATABASE_NAME)
            .addCallback(callback)
            .openHelperFactory(RequerySQLiteOpenHelperFactory())
            .build()
    }
}