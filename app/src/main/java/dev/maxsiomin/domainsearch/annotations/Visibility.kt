package dev.maxsiomin.domainsearch.annotations

import android.view.View
import androidx.annotation.IntDef
import kotlin.annotation.AnnotationRetention.SOURCE

@Retention(SOURCE)
@IntDef(View.GONE, View.INVISIBLE, View.VISIBLE)
annotation class Visibility
