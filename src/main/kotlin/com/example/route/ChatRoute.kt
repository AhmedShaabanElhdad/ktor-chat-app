package com.example.route

import com.example.room.MemberAlreadyExistException
import com.example.room.RoomController
import com.example.session.ChatSession
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach

fun Route.chatSocket(roomController: RoomController) {
    webSocket("/chatting-sockets") {
        val session = call.sessions.get(ChatSession::class)
        if (session == null) {
            close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, ""))
            return@webSocket
        }
        try {
            roomController.joinRoom(session.userName, session.sessionId, this)

            incoming.consumeEach { frame ->
                if (frame is Frame.Text) {
                    val text = frame.readText()
                    roomController.sendMessage(session.userName,text)
//                    outgoing.send(Frame.Text("YOU SAID: $text"))
//                    if (text.equals("bye", ignoreCase = true)) {
//                        close(CloseReason(CloseReason.Codes.NORMAL, "Client said BYE"))
//                    }
                }
            }
        } catch (e: MemberAlreadyExistException) {
            call.respond(HttpStatusCode.Conflict)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            roomController.disconnect(session.userName)
        }

    }
}


fun Route.getAllMessage(roomController: RoomController) {
    get("/messages") {
        call.respond(
            HttpStatusCode.OK,
            roomController.getAllMessages()
        )
    }
}