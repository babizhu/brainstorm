package com.bbz.education.brainstorm.quiz.chatroom.player

import com.bbz.education.brainstorm.quiz.chatroom.room.Match
import io.vertx.core.http.ServerWebSocket
import io.vertx.core.logging.LoggerFactory

class Player(val name: String, val socket: ServerWebSocket) {
    var status = 0//0为可匹配，1为匹配中，2为对战中
    lateinit var match: Match
    fun sendQuestion() {
        match.getCurrentQuestion()
    }
    companion object {
        val logger = LoggerFactory.getLogger(this::class.java)!!
    }
    /**
     * 准备答题，可以被其他玩家匹配
     */
    fun ready(){
        status = 1

    }
    fun choose(choose: Int) {

        if (match != null ) {
            match.choose(this, choose)
        }else{
            logger.error("用户不能答题")
        }
    }
}