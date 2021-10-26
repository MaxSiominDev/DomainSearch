package dev.maxsiomin.domainsearch.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.maxsiomin.domainsearch.database.SearchDatabase.Companion.COLUMN_SEARCHED_DOMAIN
import dev.maxsiomin.domainsearch.database.SearchDatabase.Companion.COLUMN_STATUS
import dev.maxsiomin.domainsearch.database.SearchDatabase.Companion.HISTORY_TABLE_NAME
import dev.maxsiomin.domainsearch.repository.domainrepository.Status

/**
 * [Entity] for history table
 */
@Entity(tableName = HISTORY_TABLE_NAME)
data class SearchQuery(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    /**
     * For example: 7
     */
    @ColumnInfo(name = COLUMN_SEARCHED_DOMAIN)
    val searchedDomainId: Int,

    /**
     * For example: [Status.FOUND]
     */
    @ColumnInfo(name = COLUMN_STATUS)
    val status: Status,
)
