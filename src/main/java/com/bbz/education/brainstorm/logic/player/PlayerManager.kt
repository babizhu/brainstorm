package com.bbz.education.brainstorm.logic.player

import io.netty.channel.Channel
import io.vertx.core.logging.LoggerFactory
import java.util.concurrent.ConcurrentHashMap

object PlayerManager {

    private val log = LoggerFactory.getLogger(this::class.java)!!
    private val onlinePlayers = ConcurrentHashMap<Channel, Player>()
    fun login(ctx: Channel, name: String, password: String) {
        var oldPlayer = onlinePlayers[ctx]
        oldPlayer?.run {
            sendMsg("已经登陆了")
            sendBroadcast(onlinePlayers.map { "${it.key.id()} ${it.value.name}(${it.value.channel.id()})" }.toSet().toString())
            return
        }
        oldPlayer = onlinePlayers.values.find { it.name == name }
        oldPlayer?.run {
            sendMsgAndClose("相同用户名在另外的地方登陆，您下线了")

            onlinePlayers.remove(this.channel)
            this.channel = ctx
            onlinePlayers[ctx] = this
            sendMsg("挤掉别人后登陆成功")
            sendBroadcast(onlinePlayers.map { "${it.key.id()} ${it.value.name}(${it.value.channel.id()})" }.toSet().toString())

            return
        }

        val player = Player(name, ctx)
        onlinePlayers.putIfAbsent(ctx, player)
        player.sendMsg("登陆成功")
        sendBroadcast(onlinePlayers.map { "${it.key.id()} ${it.value.name}(${it.value.channel.id()})" }.toSet().toString())
    }

    fun sendError(player: Player, msg: String) {
        player.sendMsg(msg)
    }

    fun exit(channel: Channel) {
        onlinePlayers.remove(channel)?.run {
            exit()

        }
    }

    private fun sendBroadcast(msg: String) {
        for (player in onlinePlayers.values) {
            player.sendMsg(msg)
        }
    }

    fun dispatch(channel: Channel, cmd: String, content: String) {
        var player = onlinePlayers[channel]

        player?.run {
            try {
                when (cmd) {

                    "login"->{
                        player.sendMsgAndClose("您已经登陆了,bye")
                    }
                    "joinMatch" -> {
                        player.joinMatch(channel)
                    }
                    "findQuiz" -> {
//                        player.sendQuestion()
                    }
                    "choose" -> {
//                        player.choose(Integer.parseInt(content))

                    }
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
        }
//
//
    }

}
