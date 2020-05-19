package org.example

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.data.mongodb.core.convert.MongoCustomConversions
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.*

@Configuration
class DatabaseConfiguration {
    @Bean
    fun mongoCustomConversions(): MongoCustomConversions {
        class ZonedDateTimeReadConverter : Converter<Date, ZonedDateTime> {
            override fun convert(date: Date): ZonedDateTime = date.toInstant().atZone(ZoneOffset.UTC)
        }

        class ZonedDateTimeWriteConverter : Converter<ZonedDateTime, Date> {
            override fun convert(zonedDateTime: ZonedDateTime): Date = Date.from(zonedDateTime.toInstant())
        }

        val converters: MutableList<Converter<*, *>> = ArrayList()
        converters.add(ZonedDateTimeReadConverter())
        converters.add(ZonedDateTimeWriteConverter())
        return MongoCustomConversions(converters)
    }
}