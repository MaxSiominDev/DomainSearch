package dev.maxsiomin.domainsearch.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

@Suppress("SpellCheckingInspection")
private const val BASE_URL = "https://maxsiomin.dev/api/apps/domain_search/"

private val moshi: Moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit: Retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface DomainsApiService {

    @GET("domain_query.php")
    fun downloadDescription(
        @Query("domain")
        domain: String
    ): Call<DomainsApiQueryResult>
}

data class DomainsApiQueryResult(
    val success: Boolean,
    val result: String?,
)

object DomainsApi {
    val retrofitService: DomainsApiService by lazy {
        retrofit.create(DomainsApiService::class.java)
    }
}
