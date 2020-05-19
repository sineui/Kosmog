package org.example.graphql

import graphql.GraphQL
import graphql.scalars.ExtendedScalars
import graphql.schema.GraphQLSchema
import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.SchemaParser
import org.springframework.context.annotation.Bean
import org.springframework.core.io.Resource
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.stereotype.Service

@ExperimentalStdlibApi
@Service
class GraphQLProvider(
    private val graphQLScalars: GraphQLScalars
) {
    @Bean
    fun graphQL(): GraphQL {
        val graphqls = PathMatchingResourcePatternResolver()
            .getResources("classpath*:graphqls/*.*")

        val sdl = sdlFrom(graphqls)
    }

    private fun sdlFrom(graphqls: Array<Resource>): GraphQLSchema {
        val sdl = graphqls.fold(StringBuilder()) { sb, graphql ->
            graphql.inputStream.use {
                sb.append(it.readBytes().decodeToString())
            }
        }.toString()

        val typeRegistry = SchemaParser().parse(sdl)
        val runtimeWiring = RuntimeWiring.newRuntimeWiring()
            .scalar(ExtendedScalars.DateTime)
            .scalar(graphQLScalars.objectId())
    }
}