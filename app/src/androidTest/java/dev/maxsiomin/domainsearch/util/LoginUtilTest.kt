package dev.maxsiomin.domainsearch.util

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginUtilTest {

    private lateinit var email: Email
    private lateinit var password: Password

    @Test
    fun incorrectEmailFails() {
        email = Email("something")
        assertFalse(email.isCorrect)
        email = Email("something.")
        assertFalse(email.isCorrect)
        email = Email("something.something")
        assertFalse(email.isCorrect)
        email = Email("something.something@")
        assertFalse(email.isCorrect)
        email = Email("something@.")
        assertFalse(email.isCorrect)
        email = Email("")
        assertFalse(email.isCorrect)
    }

    @Test
    fun correctEmailPasses() {
        email = Email("something@something.something")
        assertTrue(Email("something@something.something").isCorrect)
    }

    @Test
    fun emptyPasswordFails() {
        password = Password("")
        assertFalse(password.isStrong)
        assertTrue(password.isEmpty)
    }

    @Test
    fun shortPasswordFails() {
        password = Password("qwerty")
        assertFalse(password.isStrong)
        assertFalse(password.isEmpty)
    }

    @Test
    fun goodPasswordPasses() {
        password = Password("12co78qw9836")
        assertTrue(password.isStrong)
        assertFalse(password.isEmpty)
    }
}
