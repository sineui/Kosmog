package org.example.datafetchers

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

fun <T> monoDataFetcher(wrapped: (DataFetchingEnvironment) -> Mono<T>) = DataFetcher {
    wrapped(it).toFuture()
}

fun <T> fluxDataFetcher(wrapped: (DataFetchingEnvironment) -> Flux<T>) = DataFetcher {
    wrapped(it).collectList().toFuture()
}
