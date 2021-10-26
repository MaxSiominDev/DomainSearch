package dev.maxsiomin.domainsearch.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * [Dao] for [SearchDatabase]
 */
@Dao
interface SearchDao {

    /**
     * Inserts a [domain] into database
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDomain(domain: Domain)

    /**
     * Returns description of [desiredDomain]
     */
    @Query(value = "SELECT domainDescription FROM 'cachingTable' WHERE domain=:desiredDomain")
    suspend fun getDomainDescription(desiredDomain: String): String?

    /**
     * Returns domain of [desiredId]
     */
    @Query(value = "SELECT domain FROM 'cachingTable' WHERE id=:desiredId")
    suspend fun findDomainById(desiredId: Int): String?

    /**
     * Returns id of [desiredDomain]
     */
    @Query(value = "SELECT id FROM 'cachingTable' WHERE domain=:desiredDomain")
    suspend fun findIdByDomain(desiredDomain: String): Int?


    /**
     * Inserts a [searchQuery] to [SearchDatabase]
     */
    @Insert
    suspend fun insertQuery(searchQuery: SearchQuery)

    /**
     * Returns list queries in [SearchDatabase]
     */
    @Query(value = "SELECT * FROM 'historyTable'")
    suspend fun loadHistory(): List<SearchQuery>

    @Query(value = "SELECT COUNT(id) FROM 'historyTable'")
    suspend fun getHistoryLength(): Int

    /**
     * Clears history
     */
    @Query(value = "DELETE FROM 'historyTable'")
    suspend fun clearHistory()

}
