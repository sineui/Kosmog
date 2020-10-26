package org.example.graphql

import graphql.GraphQL
import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.SchemaParser
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.support.PathMatchingResourcePatternResolver

@ExperimentalStdlibApi
@Configuration
class GraphQLConfiguration(
    private val runtimeWiring: RuntimeWiring
) {
    @Bean
    fun graphQL(): GraphQL {
        val typeRegistry = SchemaParser().parse(sdl())
        val schema = SchemaGenerator().makeExecutableSchema(typeRegistry, runtimeWiring)

        return GraphQL.newGraphQL(schema).build()
    }

    private fun sdl(): String {
        return PathMatchingResourcePatternResolver()
            .getResources("classpath*:graphqls/**/.graphql")
            .fold(StringBuilder()) { sb, graphql ->
                graphql.inputStream.use { sb.append(it.readBytes().decodeToString()) }
            }.toString()
    }
}
