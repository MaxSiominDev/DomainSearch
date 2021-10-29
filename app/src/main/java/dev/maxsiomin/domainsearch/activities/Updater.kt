package dev.maxsiomin.domainsearch.activities

/**
 * Activity may implement this interface in order to update application
 */
interface Updater {

    /**
     * Called when user submits they'd like to update
     */
    fun update()
}
