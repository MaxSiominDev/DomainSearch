package dev.maxsiomin.domainsearch.util

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ExtensionsKtAndroidTest {

    @Test
    fun stringToEditableToString_isSame() {
        val strings = listOf("", "something")

        strings.forEach { string ->
            assertThat(string.toEditable().toString()).isEqualTo(string)
        }
    }

    @Test
    fun removeAllSpacesFromString() {
        assertThat("s o me t hi n g   ".removeAll(' ')).isEqualTo("something")
    }
}
