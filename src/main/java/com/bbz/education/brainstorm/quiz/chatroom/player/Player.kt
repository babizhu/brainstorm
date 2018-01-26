package com.bbz.education.brainstorm.quiz.chatroom.player

import com.bbz.education.brainstorm.quiz.chatroom.match.Match
import io.vertx.core.http.ServerWebSocket
import io.vertx.core.logging.LoggerFactory
class TimeoutEventManager(){
    val timeoutEventMap= HashMap<Long,Int>()//<超时事件类型,用于cancle的ID>

}
class Player(val name: String, val socket: ServerWebSocket) {
    val timeoutEventManager = TimeoutEventManager()
    var status = 0//0为可匹配，1为匹配中，2为对战中
    lateinit var match: Match
    fun sendQuestion() {
        match.sendCurrentQuestion()
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

    fun setTimeout(eventId:Int, timeoutId:Long ){
//        timeoutEventManager.setTimeout(timeoutId,eventId)
    }

    fun setTimeout(timeoutId:Long ){
        var get = timeoutEventManager.timeoutEventMap.get(timeoutId)
        if( get == null ){
            return
        }
        when(get) {
            1 -> chooseTimeout()
        }
    }

    /**
     * 玩家回答问题超时触发的方法
     */
    fun chooseTimeout(){
        choose(-1)
    }
    fun choose(choose: Int) {

        if (match != null ) {
            match.choose(this, choose)
        }else{
            logger.error("用户不能答题")
        }
    }
}