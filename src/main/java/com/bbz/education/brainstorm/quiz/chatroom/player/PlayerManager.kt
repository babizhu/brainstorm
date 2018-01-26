package com.bbz.education.brainstorm.quiz.chatroom.player

import com.bbz.education.brainstorm.quiz.chatroom.match.Match
import io.vertx.core.http.ServerWebSocket
import io.vertx.core.logging.LoggerFactory
import java.util.concurrent.ConcurrentHashMap

object PlayerManager {
    private val onlineUserMap = ConcurrentHashMap<ServerWebSocket, Player>()

    private val logger = LoggerFactory.getLogger(this::class.java)!!


    fun login(socket: ServerWebSocket, name: String, password: String) {
        val player = onlineUserMap[socket]
        if (player == null) {
            var player = Player(name, socket)
            onlineUserMap[socket] = player
        } else {

            logger.error("用户{${player.name}}不能用名字{$name}反复登陆")
        }
    }

    fun get(webSocket: ServerWebSocket): Player? {
        return onlineUserMap[webSocket]
    }

    /**
     * 匹配用户
     */
    fun joinMatch(me: Player) {
        if(me.status != 0 ){
            logger.error("${me.name}已经在匹配或者已经开始比赛，不能匹配")
            return
        }
        for (player in onlineUserMap.values) {//对手
            if (player != me && player.status == 1) {
                player.status = 2
                me.status = 2
                val match = Match(1, listOf(player,me))
                player.match = match
                me.match = match
                match.sendCurrentQuestion()
                return
            }
        }
        me.ready()
    }

    /**
     * 用户掉线或者退出
     */
    fun exit(player: Player) {
        onlineUserMap.remove(player.socket)
    }


}