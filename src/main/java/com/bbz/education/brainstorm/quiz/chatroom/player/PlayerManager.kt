package com.bbz.education.brainstorm.quiz.chatroom.player

import java.util.concurrent.ConcurrentHashMap

class PlayerManager {
    private val onlineUserMap = ConcurrentHashMap<String, Player>()
    fun login(name: String, password: String): Player {
        val player = onlineUserMap[name]
        return if (player != null) {
            player
        } else {
//            checkLogin()
            var player1 = Player(name)
            onlineUserMap[name] = player1
            player1
        }
    }



}