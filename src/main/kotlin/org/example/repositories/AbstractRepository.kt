package org.example.repositories

import com.mongodb.client.result.DeleteResult
import org.bson.types.ObjectId
import org.example.model.MongoDocument
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.FindAndModifyOptions
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Query.query
import org.springframework.data.mongodb.core.query.Update
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

abstract class AbstractRepository<T : MongoDocument>(
    private val collectionName: String,
    private val klass: Class<T>
) {
    @Autowired
    private lateinit var template: ReactiveMongoTemplate

    open fun find(query: Query): Flux<T> = template.find(query, klass, collectionName)
    open fun exists(query: Query): Mono<Boolean> = template.exists(query, klass, collectionName)
    open fun findOne(query: Query): Mono<T> = template.findOne(query, klass, collectionName)
    open fun findById(id: ObjectId): Mono<T> = template.findById(id, klass, collectionName)
    open fun findByIds(ids: List<ObjectId>): Flux<T> =
        template.find(query(where("_id").`in`(ids)), klass, collectionName)

    open fun findAll(): Flux<T> = template.findAll(klass, collectionName)
    open fun findAndModify(query: Query, update: Update): Mono<T> =
        template.findAndModify(query, update, FindAndModifyOptions.options().returnNew(true), klass, collectionName)

    open fun count(query: Query): Mono<Long> = template.count(query, klass, collectionName)
    open fun remove(query: Query): Mono<DeleteResult> = template.remove(query, klass, collectionName)
    open fun findAllAndRemove(query: Query): Flux<T> = template.findAllAndRemove(query, klass, collectionName)
    open fun save(t: T): Mono<T> = template.save(t, collectionName)
    open fun insert(t: T): Mono<T> = template.insert(t, collectionName)
    open fun insertAll(list: List<T>): Flux<T> = list.map(::insert).fold(Flux.empty()) { flux, t -> flux.concatWith(t) }
}
