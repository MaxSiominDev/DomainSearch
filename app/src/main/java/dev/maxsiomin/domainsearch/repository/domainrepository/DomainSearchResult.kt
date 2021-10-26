package dev.maxsiomin.domainsearch.repository.domainrepository

/**
 * Search result of [DomainRepository]
 */
sealed class DomainSearchResult(open val domain: String)

data class Success (

    override val domain: String,

    /**
     * is null when [status] = [Status.NOT_FOUND]
     */
    val description: String?,

    val status: Status,

) : DomainSearchResult(domain)

/**
 * Retrofit failure
 */
data class Failure (

    override val domain: String,

    /**
     * True if error is because of internet connection
     */
    val connectionError: Boolean,

    val errorMessage: String?,

) : DomainSearchResult(domain)
