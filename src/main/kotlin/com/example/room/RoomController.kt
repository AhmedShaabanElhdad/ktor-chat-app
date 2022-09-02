package com.example.room

import com.example.data.MessageDataSource
import com.example.data.model.Message
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap

class RoomController(
    private val messageDataSource: MessageDataSource
) {

    private val members = ConcurrentHashMap<String, Member>()

    fun joinRoom(
        userName: String,
        sessionId: String,
        socket: WebSocketSession
    ) {
        if (members.contains(userName))
            throw MemberAlreadyExistException()
        members[userName] = Member(userName, sessionId, socket)
    }

    suspend fun sendMessage(senderUserName: String, message: String) {
        members.values.forEach { member ->
            val messageEntity = Message(
                username = senderUserName,
                message = message,
                timsamp = System.currentTimeMillis()
            )

            messageDataSource.insertMessage(messageEntity)
            val parsedMessage = Json.encodeToString(messageEntity)
            member.socket.send(Frame.Text(parsedMessage))
        }
    }


    suspend fun getAllMessages():List<Message>{
        return messageDataSource.getAllMessage()
    }

    suspend fun disconnect(userName: String){
        members[userName]?.socket?.close()
        if (members.contains(userName))
            members.remove(userName)
    }

}