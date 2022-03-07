package com.sokind.data.remote.base

import com.sokind.data.repository.token.TokenRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.CompletableTransformer
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleTransformer
import retrofit2.HttpException

fun Completable.transformCompletableToSingleDefault(): Single<BaseNetworkCallResult<Unit>> =
    toSingleDefault(Unit)
        .compose(wrappingSingleNetworkCallResult())

fun <T> Single<T>.wrappingAPICallResult(): Single<BaseNetworkCallResult<T>> =
    compose(wrappingSingleNetworkCallResult())

private fun <T> wrappingSingleNetworkCallResult() =
    SingleTransformer<T, BaseNetworkCallResult<T>> { single ->
        single
            .map { data -> BaseNetworkCallResult(data) }
            .onErrorReturn { BaseNetworkCallResult(throwable = it) }
    }

fun <T> errorHandlerSingle(tokenRepository: TokenRepository) =
    SingleTransformer<T, T> { single ->
        single
            .retryWhen { error ->
                return@retryWhen error
                    .flatMapSingle {
                        if (it is HttpException) {
                            if (it.code() == 401) {
                                return@flatMapSingle tokenRepository
                                    .checkToken()
                                    .andThen(Single.just(Unit))
                            }
                        }
                        return@flatMapSingle Single.error(it)
                    }
            }
    }

fun errorHandlerCompletable(tokenRepository: TokenRepository) =
    CompletableTransformer { completable ->
        completable
            .retryWhen { error ->
                return@retryWhen error
                    .flatMapSingle {
                        if (it is HttpException) {
                            if (it.code() == 401) {
                                return@flatMapSingle tokenRepository
                                    .checkToken()
                                    .andThen(Single.just(Unit))
                            }
                        }
                        return@flatMapSingle Single.error(it)
                    }
            }
    }