package dev.maxsiomin.domainsearch.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.maxsiomin.domainsearch.database.SearchDatabase.Companion.VERSION

/**
 * Contains domains, that were already searched (cached); Contains search history
 */
@Database(entities = [Domain::class, SearchQuery::class], version = VERSION)
abstract class SearchDatabase : RoomDatabase() {

    /**
     * Returns new instance of [SearchDao]
     */
    abstract fun searchDao(): SearchDao

    companion object {

        const val VERSION = 1
        private const val DATABASE_NAME = "cachingDatabase"

        // For caching table
        const val CACHING_TABLE_NAME = "cachingTable"
        const val COLUMN_DOMAIN = "domain"
        const val COLUMN_DESCRIPTION = "domainDescription"

        // For history table
        const val HISTORY_TABLE_NAME = "historyTable"
        const val COLUMN_SEARCHED_DOMAIN = "searchedDomain"
        const val COLUMN_STATUS = "status"

        @Volatile
        private var INSTANCE: SearchDatabase? = null

        /**
         * Returns instance of [SearchDatabase]
         */
        fun getInstance(context: Context): SearchDatabase {

            synchronized(this) {

                var instance = INSTANCE

                // If instance is `null` make a new database instance
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context,
                        SearchDatabase::class.java,
                        DATABASE_NAME
                    ).fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }

                // Return instance; smart cast to be non-null
                return instance
            }
        }
    }
}
