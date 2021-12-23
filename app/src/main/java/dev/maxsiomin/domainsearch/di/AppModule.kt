package dev.maxsiomin.domainsearch.di

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.maxsiomin.domainsearch.fragments.history.HistoryLoader
import dev.maxsiomin.domainsearch.util.UiActions
import dev.maxsiomin.domainsearch.util.UiActionsImpl
import javax.inject.Singleton

/**
 * AppModule for DI
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideUiActions(@ApplicationContext context: Context): UiActions = UiActionsImpl(context)

    @Singleton
    @Provides
    fun provideHistoryLoader(@ApplicationContext context: Context): HistoryLoader =
        HistoryLoader(provideUiActions(context))

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()


    @Singleton
    @Provides
    fun provideAnalytics(): FirebaseAnalytics = Firebase.analytics
}
