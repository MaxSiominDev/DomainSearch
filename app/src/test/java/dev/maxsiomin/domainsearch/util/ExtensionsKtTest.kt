package dev.maxsiomin.domainsearch.util

import com.google.common.truth.Truth.assertThat
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test

class ExtensionsKtTest {

    @Test
    fun `surrounded o with l equals lol`() {
        assertThat("o".surroundWith('l')).isEqualTo("lol")
    }

    @Test
    fun `incorrect domain fails`() {
        assertFalse("com7".isCorrectAsDomain())
    }

    @Test
    fun `correct domain passes`() {
        assertTrue("com".isCorrectAsDomain())
        assertTrue(".com".isCorrectAsDomain())
    }

    @Test
    fun `word something contains letters of abc only  is true`() {
        assertFalse("something".containsNot('a'..'z'))
    }

    @Test
    fun `word something contains digits only is false`() {
        assertTrue("something".containsNot('0'..'9'))
    }

    @Test
    fun `add dot to com as prefix equals dotcom`() {
        assertThat("com".addPrefix(".")).isEqualTo(".com")
    }

    @Test
    fun `not null works properly`() {
        assertThat(null.notNull()).isEqualTo("")
        assertThat("".notNull()).isEqualTo("")
        assertThat("com".notNull()).isEqualTo("com")
    }

    @Test
    fun `overridden operator contains works properly`() {
        assertTrue("something".contains('a'..'z'))
        assertFalse("something".contains('0'..'9'))
    }
}
