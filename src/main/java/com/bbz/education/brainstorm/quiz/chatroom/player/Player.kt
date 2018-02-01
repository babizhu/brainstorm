package com.bbz.education.brainstorm.quiz.chatroom.player

import com.bbz.education.brainstorm.logic.match.Match
import io.vertx.core.Vertx
import io.vertx.core.http.ServerWebSocket
import io.vertx.core.logging.LoggerFactory

class TimeoutEventManager() {
    val timeoutEventMap = HashMap<Long, Int>()//<超时事件类型,用于cancle的ID>

}

class Player(val name: String, val socket: ServerWebSocket, val playerManager: PlayerManager) {
    val timeoutEventManager = TimeoutEventManager()
    var status = 0//0为可匹配，1为匹配中，2为对战中
    lateinit var match: Match
    fun sendQuestion() {
        match.sendCurrentQuestion()
    }

    companion object {
        val logger = LoggerFactory.getLogger(this::class.java)!!
    }

    fun joinMatchSuccess(){

    }

    /**
     * 准备答题，可以被其他玩家匹配
     */
    fun joinMatchPending(vertx: Vertx) {
        status = 1
        setTimeout(vertx,1)
    }

    private fun setTimeout(vertx: Vertx, eventId: Int) {
        var timerId = vertx.setTimer(12000, {
            var event = timeoutEventManager.timeoutEventMap.remove(it)
            if (event != null) {
                when(event){
                    1->joinMatchTimeout()
                }
            }

        })
        timeoutEventManager.timeoutEventMap[timerId] = eventId
    }

    fun setTimeout(eventId: Int, timeoutId: Long) {
//        timeoutEventManager.setTimeout(timeoutId,eventId)
    }

    private fun joinMatchTimeout(){
        socket.writeTextMessage("放弃Joinmatch")
        status = 0
    }

    fun setTimeout(timeoutId: Long) {
        var get = timeoutEventManager.timeoutEventMap.get(timeoutId)
        if (get == null) {
            return
        }
        when (get) {
            1 -> chooseTimeout()
        }
    }

    /**
     * 玩家回答问题超时触发的方法
     */
    fun chooseTimeout() {
        choose(-1)
    }

    fun choose(choose: Int) {

        if (match != null) {
            match.choose(this, choose)
        } else {
            logger.error("用户不能答题")
        }
    }

    fun joinMatch(vertx:Vertx) {
        if(status != 0 ){
            logger.error("${name}已经在匹配或者已经开始比赛，不能匹配")
            return
        }
        for (player in PlayerManager.onlineUserMap.values) {//对手

            if (player != this && player.status == 1) {
                player.status = 2
                status = 2
                val match = Match(1, listOf(player,this))

                player.match = match
                this.match = match

                match.sendCurrentQuestion()

                return
            }
        }
        joinMatchPending(vertx)
    }

    fun exit() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}