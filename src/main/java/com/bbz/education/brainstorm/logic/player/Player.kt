package com.bbz.education.brainstorm.logic.player

import io.netty.channel.Channel
import io.netty.channel.ChannelFutureListener
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame

class Player(val name: String, var channel: Channel,status:Int=0, val level: Int=1) {
    var point: Int = 0
    fun sendMsg(msg: String) {
        channel.writeAndFlush(TextWebSocketFrame(msg))
    }

    fun sendMsgAndClose(msg: String) {
        channel.writeAndFlush(TextWebSocketFrame(msg)).addListener(ChannelFutureListener.CLOSE)
    }

    private fun changePoint(change: Int) {
        if (change < 0 && this.point < -change) {
            point = 0
        } else {
            this.point += change
        }
    }

    /**
     * 退出
     */
    fun exit() {
        channel.close()
        save()
    }

    /**
     * 保存用户数据
     */
    private fun save() {

    }

    fun joinMatch(channel: Channel) {
        
    }

    @Synchronized
    fun makePairSuccess() {
        point -=100
    }

}