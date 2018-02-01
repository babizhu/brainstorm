package com.bbz.education.brainstorm.network.netty.logic.handler

import io.netty.channel.Channel
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame

abstract class BaseHandler : SimpleChannelInboundHandler<TextWebSocketFrame>() {
    lateinit var channel: Channel

    fun write(msg:String){
        channel.writeAndFlush(TextWebSocketFrame(msg))
    }

    fun writeAndClose(msg:String){
        channel.writeAndFlush(TextWebSocketFrame(msg)).addListener(ChannelFutureListener.CLOSE)
    }
    override fun channelActive(ctx: ChannelHandlerContext) {
        super.channelActive(ctx)
        this.channel = ctx.channel()
    }
}