package com.example.plugins

import com.example.room.RoomController
import com.example.route.chatSocket
import com.example.route.getAllMessage
import io.ktor.server.routing.*
import io.ktor.server.application.*
import org.koin.java.KoinJavaComponent.inject

fun Application.configureRouting() {
    val roomController:RoomController by inject(RoomController::class.java)
    install(Routing){
        chatSocket(roomController)
        getAllMessage(roomController)
    }

}
