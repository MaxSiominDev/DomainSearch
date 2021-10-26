package dev.maxsiomin.domainsearch.repository.updaterepository

sealed class LastVersionSearchResult

data class Success(val latestVersionName: String) : LastVersionSearchResult()

data class Failure (

    val connectionError: Boolean,

    val errorMessage: String?,

) : LastVersionSearchResult()
