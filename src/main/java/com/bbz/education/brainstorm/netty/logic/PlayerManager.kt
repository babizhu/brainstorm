package com.bbz.education.brainstorm.netty.logic

import io.netty.channel.ChannelHandlerContext
import io.vertx.core.logging.LoggerFactory
import java.util.concurrent.ConcurrentHashMap

object PlayerManager {

    private val log = LoggerFactory.getLogger(this::class.java)!!
    private val onlinePlayers = ConcurrentHashMap<ChannelHandlerContext, Player>()
    fun login(ctx: ChannelHandlerContext, name: String, password: String) {
        var oldPlayer = onlinePlayers[ctx]
        oldPlayer?.run {
            sendMsg("已经登陆了")
            sendBroadcast(onlinePlayers.map { "${it.key.channel().id()} ${it.value.name}(${it.value.ctx.channel().id()})"}.toSet().toString())

            return
        }
        oldPlayer = onlinePlayers.values.find { it.name == name }
        oldPlayer?.run {
            sendMsgAndClose("相同用户名在另外的地方登陆，您下线了")

            onlinePlayers.remove(this.ctx)
            this.ctx = ctx
            onlinePlayers[ctx] = this
            sendMsg("挤掉别人后登陆成功")
            sendBroadcast(onlinePlayers.map { "${it.key.channel().id()} ${it.value.name}(${it.value.ctx.channel().id()})"}.toSet().toString())

            return
        }

        var player = Player(name, ctx)
        onlinePlayers.putIfAbsent(ctx, player)
        player.sendMsg("登陆成功")
        sendBroadcast(onlinePlayers.map { "${it.key.channel().id()} ${it.value.name}(${it.value.ctx.channel().id()})"}.toSet().toString())
    }

    fun sendError(player: Player, msg: String) {
        player.sendMsg(msg)
    }

    fun exit(ctx: ChannelHandlerContext){
        onlinePlayers.remove(ctx)?.run {
            exit()

        }
    }

    private fun sendBroadcast(msg:String){
        for (player in onlinePlayers.values) {
            player.sendMsg(msg)
        }
    }

}