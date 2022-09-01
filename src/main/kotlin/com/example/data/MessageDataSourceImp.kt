package com.example.data

import com.example.data.model.Message
import com.mongodb.client.MongoDatabase
import org.litote.kmongo.KMongo
import org.litote.kmongo.coroutine.CoroutineDatabase


class MessageDataSourceImp(
    database: CoroutineDatabase
) : MessageDataSource {

    private val messages = database.getCollection<Message>()

    override suspend fun getAllMessage(): List<Message> {
        return messages.find()
            .descendingSort(Message::id)
            .toList()
    }

    override suspend fun insertMessage(message: Message) {
        messages.insertOne(message)
    }

}