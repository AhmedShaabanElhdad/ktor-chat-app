package com.example.data.model

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Message (
    val username:String,
    val message:String,
    val timsamp:Long,
    @BsonId
    val id:String = ObjectId().toString(),
)