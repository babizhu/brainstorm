package com.bbz.education.brainstorm.netty

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler


class TestHandler : SimpleChannelInboundHandler<Any>() {
    override fun channelRead0(ctx: ChannelHandlerContext, msg: Any) {
        println("TestHandler.channelRead0")
        ctx.fireChannelRead(msg)
    }

    override fun handlerAdded(ctx: ChannelHandlerContext) {
        println("TestHandler.handlerAdded")
        super.handlerAdded(ctx)
    }

    override fun handlerRemoved(ctx: ChannelHandlerContext) {
        println("TestHandler.handlerRemoved")

        super.handlerRemoved(ctx)
    }
}