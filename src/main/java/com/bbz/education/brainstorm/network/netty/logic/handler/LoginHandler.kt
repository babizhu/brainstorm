package com.bbz.education.brainstorm.network.netty.logic.handler

import com.bbz.education.brainstorm.logic.player.PlayerManager
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame

class LoginHandler  : BaseHandler() {
    override fun channelRead0(ctx: ChannelHandlerContext, msg: TextWebSocketFrame) {
        println(msg.text())
        var text = msg.text()
        val indexOf = text.indexOf("|")
        val cmd = text.substring(0, indexOf)
        val content = text.substring(indexOf + 1, text.length)
        if( cmd != "login"){
            writeAndClose("尚未登陆")
        }else{
            PlayerManager.login(channel,content,"")
            ctx.pipeline().remove("loginHandler")
            ctx.pipeline().addLast("logicHandler", BrainStormHandler())
        }


    }


    override fun channelInactive(ctx: ChannelHandlerContext) {
//        PlayerManager.exit(channel)
    }

    @Throws(Exception::class)
    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        cause.printStackTrace()
        ctx.channel().close()
    }
}