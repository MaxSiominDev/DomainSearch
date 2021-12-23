package dev.maxsiomin.domainsearch.extensions

import com.google.firebase.auth.FirebaseUser

val FirebaseUser.isNotEmailVerified get() = !isEmailVerified
