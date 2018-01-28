package com.bbz.education.brainstorm.quiz.chatroom.player

import com.bbz.education.brainstorm.quiz.chatroom.match.Match
import io.vertx.core.http.ServerWebSocket
import io.vertx.core.logging.LoggerFactory
import java.util.concurrent.ConcurrentHashMap

object PlayerManager {
    val onlineUserMap = ConcurrentHashMap<ServerWebSocket, Player>()

    private val logger = LoggerFactory.getLogger(this::class.java)!!


    fun login(socket: ServerWebSocket, name: String, password: String) {
        val player = onlineUserMap[socket]
        if (player == null) {
            var player = Player(name, socket,this)
            onlineUserMap[socket] = player
        } else {

            logger.error("用户{${player.name}}不能用名字{$name}反复登陆")
        }
    }

    fun get(webSocket: ServerWebSocket): Player? {
        return onlineUserMap[webSocket]
    }

    /**
     * 用户掉线或者退出
     */
    fun exit(player: Player) {
        onlineUserMap.remove(player.socket)
    }


}