@file:Suppress("unused")

package com.bbz.education.brainstorm.quiz.chatroom

import com.bbz.education.brainstorm.quiz.chatroom.player.PlayerManager
import io.vertx.core.Vertx
import io.vertx.core.VertxOptions
import io.vertx.core.http.HttpServer
import io.vertx.core.http.ServerWebSocket
import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.web.Router
import io.vertx.kotlin.coroutines.CoroutineVerticle
import java.util.*

/**
 * ChatroomMainVerticle
 * ---------------------------
 */
class ChatroomMainVerticle : CoroutineVerticle() {
    private val connectionMap = HashMap<String, ServerWebSocket>(16)

    companion object {
        val logger = LoggerFactory.getLogger(this::class.java)!!
    }


    override suspend fun start() {
        val server = vertx.createHttpServer()

        val router = Router.router(vertx)

        router.route("/").handler({ routingContext -> routingContext.response().sendFile("html/ws.html") })
        websocketMethod(server)
        server.requestHandler(router::accept).listen(8080)
    }

    private fun websocketMethod(server: HttpServer) {

        server.websocketHandler({ webSocket ->

            if (webSocket.path() != "/chat") {
//                webSocket.writeTextMessage("非法请求")

                webSocket.reject()
            }
            // 获取每一个链接的ID
            val id = webSocket.binaryHandlerID()
            // 判断当前连接的ID是否存在于map集合中，如果不存在则添加进map集合中
            if (!checkID(id)) {
                connectionMap[id] = webSocket
            }

            for (entry in connectionMap) {
//                    if (currID != entry.key) {
                /* 发送文本消息
            文本信息似乎不支持图片等二进制消息
            若要发送二进制消息，可用writeBinaryMessage方法
            */
//                entry.value.writeTextMessage("$id 进入聊天室")
//                    }
            }


            //　WebSocket 连接
//            webSocket.frameHandler({ handler ->
//
//                val textData = handler.textData()
//                //给非当前连接到服务器的每一个WebSocket连接发送消息
//                for (entry in connectionMap) {
////                    if (currID != entry.key) {
//                    /* 发送文本消息
//                文本信息似乎不支持图片等二进制消息
//                若要发送二进制消息，可用writeBinaryMessage方法
//                */
//                    entry.value.writeTextMessage(textData)
////                    }
//                }
//
//            })

            webSocket.endHandler({it-> println("endhandler $it")})
            webSocket.textMessageHandler({ msg ->
                var indexOf = msg.indexOf("|")
                val cmd = msg.substring(0, indexOf)
                val content = msg.substring(indexOf + 1, msg.length)
                val player = PlayerManager.get(webSocket)
                try {

                when (cmd) {
                    "login" -> {
                        PlayerManager.login(webSocket, content, "")
                    }
                    "joinMatch" -> {
                        if (player != null) {
                            PlayerManager.joinMatch(player)
                            vertx.setTimer(12000,{
                                if(player.status == 1){
                                    player.socket.writeTextMessage("放弃Joinmatch")
                                    player.status = 0
                                }
                            })
                        }
                    }
                    "findQuiz" -> {
                        player?.sendQuestion()
                    }
                    "choose" -> {
                        player?.choose(Integer.parseInt(content))
                    }
                }
                }catch ( exception:Exception){
                    exception.printStackTrace()
                }
            })
            webSocket.exceptionHandler { it ->
                it.printStackTrace()
            }
            // 客户端WebSocket 关闭时，将当前ID从map中移除
            webSocket.closeHandler({
                val player = PlayerManager.get(webSocket)
                if(player != null ){
                    PlayerManager.exit(player)
                }
            })

        })
    }

    // 检查当前ID是否已经存在与map中
    private fun checkID(id: String): Boolean {
        return connectionMap.containsKey(id)
    }
}
//
//fun Route.coroutineHandler(fn: suspend (RoutingContext) -> Unit) {
//    handler { ctx ->
//        launch(ctx.vertx().dispatcher()) {
//            try {
//                fn(ctx)
//            } catch (e: Exception) {
//                ctx.fail(e)
//            }
//        }
//    }
//}

fun main(args: Array<String>) {
//    System.setProperty("vertx.logger-delegate-factory-class-name",
//            "io.vertx.core.logging.Log4j2LogDelegateFactory")
    val vertxOptions = VertxOptions()
    vertxOptions.blockedThreadCheckInterval = 1000000
    val vertx = Vertx.vertx(vertxOptions)
    vertx.deployVerticle(ChatroomMainVerticle())
}