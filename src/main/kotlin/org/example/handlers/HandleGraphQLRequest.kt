package org.example.handlers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import graphql.ExecutionInput.newExecutionInput
import graphql.ExecutionResult
import graphql.GraphQL
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.badRequest
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono
import reactor.core.publisher.Mono.fromCompletionStage
import reactor.core.publisher.Mono.subscriberContext
import reactor.kotlin.core.util.function.component1
import reactor.kotlin.core.util.function.component2
import reactor.util.context.Context
import reactor.util.function.Tuple2
import java.net.URLDecoder

/**
 * Reference: https://github.com/geowarin/graphql-webflux
 */

@Component
class HandleGraphQLRequest(private val graphQL: GraphQL) : (ServerRequest) -> (Mono<ServerResponse>) {
    private val mapper = jacksonObjectMapper()
    private val graphQLMediaType: MediaType = MediaType.parseMediaType("application/graphql")

    override fun invoke(request: ServerRequest): Mono<ServerResponse> {
        fun buildResponse(result: ExecutionResult): Mono<ServerResponse> {
            return if (result.errors.isEmpty()) {
                ok().bodyValue(result)
            } else {
                badRequest().bodyValue(result)
            }
        }

        return Mono.just(graphParametersFrom(request))
            .zipWith(subscriberContext())
            .flatMap(::executeGraphQLQuery)
            .flatMap(::buildResponse)
    }

    private fun executeGraphQLQuery(tuple2: Tuple2<GraphQLParameters, Context>): Mono<ExecutionResult> {
        val (graphQLParameters, context) = tuple2
        val executionInput = newExecutionInput()
            .query(graphQLParameters.query)
            .operationName(graphQLParameters.operationName)
            .variables(graphQLParameters.variables)
            .context(context)
        return fromCompletionStage(graphQL.executeAsync(executionInput))
    }

    /**
     * Reference: https://graphql.org/learn/serving-over-http/
     * If the "query" query string parameter is present (as in the GET example above), it should be parsed and handled in the same way as the HTTP GET case.
     * If the "application/graphql" Content-Type header is present, treat the HTTP POST body contents as the GraphQL query string.
     */

    private fun graphParametersFrom(request: ServerRequest): GraphQLParameters {
        return when {
            request.queryParam("query").isPresent -> parseFromRequestParameters(request)
            request.method() == HttpMethod.POST -> parseFromPostBody(request)
            else -> throw IllegalArgumentException("Not a GraphQL request.")
        }
    }

    private fun parseFromRequestParameters(request: ServerRequest) = GraphQLParameters(
        query = request.queryParam("query").get(),
        operationName = request.queryParam("operationName").orElseGet { null },
        variables = getVariables(request)
    )


    private fun getVariables(request: ServerRequest) =
        request.queryParam("variables")
            .map { URLDecoder.decode(it, "UTF-8") }
            .map { readJsonMap(it) }
            .orElseGet { emptyMap() }

    private fun parseFromPostBody(request: ServerRequest): GraphQLParameters {
        val graphql

        return if (request.headers().contentType().filter { it.isCompatibleWith(graphQLMediaType) }.isPresent) {
            request.bodyToMono<String>().flatMap { GraphQLParameters(query = it) }
        } else {
            request.bodyToMono<String>().flatMap { mapper.readValues(it, GraphQLParameters::class.java) }
        }
    }

    private fun readJsonMap(variables: String): Map<String, Any> = mapper.readValue(variables, MapTypeRef)
}

data class GraphQLParameters(
    val query: String,
    val operationName: String? = null,
    val variables: Map<String, Any> = emptyMap()
)