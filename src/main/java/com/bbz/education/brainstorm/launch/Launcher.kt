package com.bbz.education.brainstorm.launch

import com.bbz.education.brainstorm.network.netty.BrainStormServer
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors

class Launcher {
    fun start(type:Int){
        if( type == 1 ){
            BrainStormServer().start()
        }else{
            //启动vertx版本
        }
    }
}
data class Student(val name:String,val age:Int)
fun main(args: Array<String>) {
    var map = ConcurrentHashMap<Int, Student>()
    repeat(100){
        map[it] = Student("a",1)

    }
    var service = Executors.newFixedThreadPool(3)
    service.execute({
        for (entry in map) {
            println("${entry.key}-${entry.value}")
            Thread.sleep(10)
        }

    })
    service.execute({
       Thread.sleep(200)//确保第一个线程已经启动了
        map.remove(0)
        map.remove(99)
        map.remove(50)
        print("删除完毕")
    })

    service.shutdown()


//    Launcher().start(1)
}