package org.example.graphql

import graphql.schema.Coercing
import graphql.schema.GraphQLScalarType
import org.bson.types.ObjectId
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GraphQLScalars {

    /**
     * https://github.com/QUSHLEE
     * from https://github.com/codeit-kr
     */
    @Bean
    fun objectIdScalar(): GraphQLScalarType {
        val coercing = object : Coercing<ObjectId, String> {
            override fun serialize(input: Any) = input.toString()

            override fun parseValue(input: Any): ObjectId {
                return when (input) {
                    is graphql.language.StringValue -> ObjectId(input.value)
                    is String -> ObjectId(input)
                    else -> throw Exception("wrong input type")
                }
            }

            override fun parseLiteral(input: Any): ObjectId {
                return when (input) {
                    is graphql.language.StringValue -> ObjectId(input.value)
                    is String -> ObjectId(input)
                    else -> throw Exception("wrong input type")
                }
            }
        }

        return GraphQLScalarType.newScalar()
            .name("ObjectId")
            .coercing(coercing)
            .description("[`ObjectId`] Scalar represents ObjectId of Mongo documents.")
            .build()
    }
}
