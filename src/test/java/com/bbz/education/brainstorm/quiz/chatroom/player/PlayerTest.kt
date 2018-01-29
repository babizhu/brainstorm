package com.bbz.education.brainstorm.quiz.chatroom.player

import org.junit.Test
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger


data class Account(val name:String,var cash:Int){
    companion object {
        val count = AtomicInteger(0)
    }

    fun transform(from:Account, tranformCash: Int ){
        class Helper{
            fun transform(){
                if( from.cash > tranformCash){
                    cash += tranformCash
                    from.cash -= tranformCash
                    println("${count.incrementAndGet()} ${Thread.currentThread().name} ${from.name}向$name 转了$tranformCash")

                }
            }
        }
        if(from.name > this.name || true){
            synchronized(from){
                synchronized(this){
                    Helper().transform()
                }
            }
        }else{
            synchronized(this){
                synchronized(from){
                    Helper().transform()

                }
            }
        }
//        synchronized(from){
//            if( from.cash > tranformCash){
//                synchronized(this){
//                    this.cash += tranformCash
//                }
//                from.cash -= tranformCash
//                println("${count.incrementAndGet()} ${Thread.currentThread().name} ${from.name}向${this.name} 转了$tranformCash")
//            }else{
//                println("没钱了")
//            }
//        }
    }

}

class PlayerTest {



    @Test
    fun joinMatch() {
        var a = Account("甲",100000)
        var b = Account("乙",100000)

        var random = Random()
        var service = Executors.newFixedThreadPool(100)
        println("ready transform!!!!!!!!!!!")
        repeat(1000000){
            service.execute({
                if(random.nextBoolean()){
                    a.transform(b,1)
                }else{
                    b.transform(a,1)
                }
            })
        }
        service.awaitTermination(100000,TimeUnit.DAYS)
    }

}