package dev.maxsiomin.domainsearch.util

import androidx.core.os.bundleOf
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class SharedDataTest {

    @Test
    fun sharedDataPutsAndReturnsSameString() {
        val sharedData = SharedDataImpl(null)
        val testKey = StringSharedDataKey("test")
        sharedData.putSharedString(testKey, "something")
        assertThat(sharedData.getSharedString(testKey)).isEqualTo("something")
    }

    @Test
    fun stringAddedToSharedDataInBundleIsSaved() {
        val testKey = StringSharedDataKey("test")
        val sharedData = SharedDataImpl(bundleOf(testKey.value to "something"))
        assertThat(sharedData.getSharedString(testKey)).isEqualTo("something")
    }
}
