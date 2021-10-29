package dev.maxsiomin.domainsearch.repository.domainrepository

/**
 * Status for search history
 */
enum class Status {

    /**
     * Retrofit error
     */
    FAILURE,

    /**
     * Domain not found
     */
    NOT_FOUND,

    /**
     * Domain found
     */
    FOUND,
}
