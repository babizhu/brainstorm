package com.bbz.education.brainstorm.quiz.chatroom.player

import io.vertx.core.http.ServerWebSocket
import io.vertx.core.logging.LoggerFactory
import java.util.concurrent.ConcurrentHashMap

object PlayerManager {
    val onlineUserMap = ConcurrentHashMap<ServerWebSocket, Player>()

    private val logger = LoggerFactory.getLogger(this::class.java)!!


    fun login(socket: ServerWebSocket, name: String, password: String) {

//        var plus = onlineUserMap + onlineUserMap
//
////        onlineUserMap.putIfAbsent(socket,Player(name, socket,this))
//        val player = onlineUserMap[socket]
//
//        if (player == null) {
//            val player = Player(name, socket, this)
//            onlineUserMap[socket] = player
//        } else {
//            logger.error("用户{${player.name}}不能用名字{$name}反复登陆")
//        }

        onlineUserMap.computeIfAbsent(socket, {
            Player(name, socket, this)
        })
    }

    fun get(webSocket: ServerWebSocket): Player? {
        return onlineUserMap[webSocket]
    }

    /**
     * 用户掉线或者退出
     */
    fun exit(player: Player) {
        player.exit()
        onlineUserMap.remove(player.socket)
    }


}


val s = fun(i: Int): String {
    return "$i"
}

fun t(i: Int, block: (Int) -> String) {
    block(i)
}

fun main(args: Array<String>) {
//    val func = test1
    t(3, s)
    val map = HashMap<Int, String>()
    map[1] = "1"

    var pair = map to 2
    map.also { hashMap -> print(hashMap) }
    println(pair)

    val result = "Hello World".apply {
        println(this)
//  println(it)  // 编译器报错，lambda表达式没有参数it不能用
        520
    }

    println(result)

    val result1 = "Hello World".run {
        println(this)
//  println(it)  // 编译器报错，lambda表达式没有参数it不能用
        520
    }

    println(result1)
    var arrayList = ArrayList<String>()
    with(arrayList) {
        add("testWith")
        add("testWith")
        add("testWith")
        println("this = " + this)
    }.let { println(it) }


}