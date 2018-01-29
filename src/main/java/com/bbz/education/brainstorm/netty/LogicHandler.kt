package com.bbz.education.brainstorm.netty

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame
import java.time.LocalDateTime


class LogicHandler : SimpleChannelInboundHandler<TextWebSocketFrame>() {
    override fun channelRead0(ctx: ChannelHandlerContext?, msg: TextWebSocketFrame?) {
        ctx?.let {
            var channel = ctx.channel()
            println(channel.remoteAddress().toString() + ": " + msg!!.text())
            ctx.channel().writeAndFlush(TextWebSocketFrame("来自服务端: " + LocalDateTime.now()))
        }
    }

    @Throws(Exception::class)
    override fun handlerAdded(ctx: ChannelHandlerContext?) {
        println("ChannelId" + ctx!!.channel().id().asLongText())
    }

    @Throws(Exception::class)
    override fun handlerRemoved(ctx: ChannelHandlerContext?) {
        println("用户下线: " + ctx!!.channel().id().asLongText())
    }

    @Throws(Exception::class)
    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        cause.printStackTrace()
        ctx.channel().close()
    }
}