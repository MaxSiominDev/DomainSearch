package dev.maxsiomin.domainsearch.util

import retrofit2.*

fun <T> Call<T>.addOnCompleteListener(onResult: (RetrofitResult<T>) -> Unit) {
    this.enqueue(object : Callback<T> {

        override fun onFailure(call: Call<T>, t: Throwable) =
            onResult(RetrofitResult(call, null, t))

        override fun onResponse(call: Call<T>, response: Response<T>) =
            onResult(RetrofitResult(call, response, null))
    })
}

data class RetrofitResult<T> (
    val call: Call<T>,
    val response: Response<T>?,
    val t: Throwable?,
) {
    val isSuccessful = response?.isSuccessful ?: false
}
