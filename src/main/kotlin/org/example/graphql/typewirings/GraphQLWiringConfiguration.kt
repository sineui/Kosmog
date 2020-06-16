package org.example.graphql.typewirings

import graphql.scalars.ExtendedScalars
import graphql.schema.GraphQLScalarType
import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.TypeRuntimeWiring
import org.example.datafetchers.UserDataFetchers
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GraphQLWiringConfiguration(
    private val objectIdScalar: GraphQLScalarType,
    private val userDataFetchers: UserDataFetchers
) {
    @Bean
    fun runtimeWiring(): RuntimeWiring {
        return RuntimeWiring.newRuntimeWiring()
            .scalar(ExtendedScalars.DateTime)
            .scalar(objectIdScalar)
            .type(queryTypeWiring()).build()
    }

    private fun queryTypeWiring(): TypeRuntimeWiring.Builder {
        return TypeRuntimeWiring.newTypeWiring("query")
            .dataFetcher("currentUser", userDataFetchers.currentUser())
    }
}
