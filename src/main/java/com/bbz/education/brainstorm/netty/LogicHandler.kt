package com.bbz.education.brainstorm.netty

import com.bbz.education.brainstorm.netty.logic.PlayerManager
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame


class LogicHandler : SimpleChannelInboundHandler<TextWebSocketFrame>() {
    override fun channelRead0(ctx: ChannelHandlerContext, msg: TextWebSocketFrame) {
        println(msg.text())
        msg.text().run {
            val indexOf = indexOf("|")
            val cmd = substring(0, indexOf)
            val content = substring(indexOf + 1, length)


            PlayerManager.login(ctx, content, "1")
        }
    }


    override fun channelActive(ctx: ChannelHandlerContext) {

    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
        PlayerManager.exit(ctx)
    }

    @Throws(Exception::class)
    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        cause.printStackTrace()
        ctx.channel().close()
    }
}