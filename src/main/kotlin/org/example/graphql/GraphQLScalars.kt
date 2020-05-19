package org.example.graphql

import graphql.schema.Coercing
import graphql.schema.GraphQLScalarType
import org.bson.types.ObjectId
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class GraphQLScalars {

    /**
     * https://github.com/QUSHLEE
     * from https://github.com/codeit-kr
     */
    @Bean
    fun objectId(): GraphQLScalarType {
        return GraphQLScalarType.newScalar()
            .name("ObjectId")
            .coercing(coercing())
            .description("[`ObjectId`] Scalar represents ObjectId of Mongo documents.")
            .build()
    }

    private fun coercing(): Coercing<ObjectId, String> {
        return object : Coercing<ObjectId, String> {
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
    }
}