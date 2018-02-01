package com.bbz.education.brainstorm.logic.player

import io.netty.channel.socket.nio.NioSocketChannel
import org.junit.Test
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import kotlin.collections.ArrayList

class MakePairTest {


    @Test
    fun remove() {
    }

    @Test
    fun makePair() {
        var makePair = MakePair()
        var service = Executors.newFixedThreadPool(100)
        val playerCount = 3
        var testCount = 30000000
        var countDownLatch = CountDownLatch(testCount)
        var channel = NioSocketChannel()

        var list = ArrayList<Player>()
        val random = Random()
        repeat(playerCount){
            list.add(Player("bbz$it",channel,level = random.nextInt(10)))
        }

        
        repeat(testCount){
            service.execute{
//                Thread.sleep(random.nextInt(3).toLong())
                makePair.makePair(list[random.nextInt(3)])
                countDownLatch.countDown()
            }
        }
        countDownLatch.await()
        makePair.printPlayers()
    }
}