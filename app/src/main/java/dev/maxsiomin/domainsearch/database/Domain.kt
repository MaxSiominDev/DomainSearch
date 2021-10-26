package dev.maxsiomin.domainsearch.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.maxsiomin.domainsearch.database.SearchDatabase.Companion.CACHING_TABLE_NAME
import dev.maxsiomin.domainsearch.database.SearchDatabase.Companion.COLUMN_DESCRIPTION
import dev.maxsiomin.domainsearch.database.SearchDatabase.Companion.COLUMN_DOMAIN

/**
 * [Entity] for [SearchDatabase]
 */
@Entity(tableName = CACHING_TABLE_NAME)
data class Domain (
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    /**
     * For example: "au"
     */
    @ColumnInfo(name = COLUMN_DOMAIN)
    val domain: String,

    /**
     * For example: "Australia"
     */
    @ColumnInfo(name = COLUMN_DESCRIPTION)
    val description: String
)
