package com.bbz.education.brainstorm.netty.logic

import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame

class Player(val name: String, var ctx: ChannelHandlerContext) {
    var point: Int = 0
    fun sendMsg(msg: String) {
        ctx.writeAndFlush(TextWebSocketFrame(msg))
    }

    fun sendMsgAndClose(msg: String) {
        ctx.writeAndFlush(TextWebSocketFrame(msg)).addListener(ChannelFutureListener.CLOSE)
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
        ctx.close()
        save()
    }

    /**
     * 保存用户数据
     */
    private fun save() {

    }
}