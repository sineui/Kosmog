package org.example.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.mapping.Document
import java.time.ZonedDateTime

@Document
abstract class MongoDocument(
    @Id val id: ObjectId = ObjectId(),
    @CreatedDate
    open var createdDate: ZonedDateTime = ZonedDateTime.now(),
    @LastModifiedDate
    open var lastModifiedDate: ZonedDateTime = ZonedDateTime.now()
)
