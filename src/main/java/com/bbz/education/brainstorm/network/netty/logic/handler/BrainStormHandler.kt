package com.bbz.education.brainstorm.network.netty.logic.handler

import com.bbz.education.brainstorm.logic.player.PlayerManager
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame


class BrainStormHandler : BaseHandler() {
    override fun channelRead0(ctx: ChannelHandlerContext, msg: TextWebSocketFrame) {

        msg.text().run {
            val indexOf = indexOf("|")
            val cmd = substring(0, indexOf)
            val content = substring(indexOf + 1, length)

            PlayerManager.dispatch(ctx.channel(), cmd, content)

        }
    }


    @Throws(Exception::class)
    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        cause.printStackTrace()
        ctx.channel().close()
    }

}