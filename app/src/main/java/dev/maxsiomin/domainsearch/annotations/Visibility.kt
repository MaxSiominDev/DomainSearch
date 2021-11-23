package dev.maxsiomin.domainsearch.annotations

import android.view.View
import androidx.annotation.IntDef
import kotlin.annotation.AnnotationRetention.SOURCE

@Target(AnnotationTarget.TYPE)
@Retention(SOURCE)
@IntDef(View.GONE, View.INVISIBLE, View.VISIBLE)
annotation class Visibility
