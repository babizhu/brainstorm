package com.bbz.education.brainstorm.logic.player

class MakePair {

    private val players = HashSet<Player>()

    /**
     * 从待匹配玩家列表中移除指定玩家
     */
    fun remove(player: Player) {
        synchronized(players) {
            players.remove(player)
        }
    }

    fun printPlayers() {
        synchronized(players) {
            var string = players.joinToString(",") { it.name }
        println(string)
        }
    }

    fun makePair(player: Player) {
        var find: Player? = null
        synchronized(players) {
            if(players.contains(player)){
                println("${Thread.currentThread().name}-[${player.name}]不能反复匹配")

                return
            }
            var iterator = players.iterator()
            while (iterator.hasNext()) {
                var temp = iterator.next()

                if (checkCondition(temp, player)) {
                    find = temp
                    iterator.remove()
                }
            }
            if (find == null) {
                players.add(player)
                println("${Thread.currentThread().name}-[${player.name}]未匹配到玩家")
                return
            }
        }
        //匹配到了对象
        makePairSuccess(find!!, player)
    }

    /**
     * 这里需要注意player加锁的顺序，以免造成死锁
     * player的函数内部有加锁
     */
    private fun makePairSuccess(temp: Player, player: Player) {

        if (temp.name < player.name) {
            player.makePairSuccess()
            temp.makePairSuccess()
        } else {
            temp.makePairSuccess()
            player.makePairSuccess()
        }
    }

    /**
     * 检测两个玩家是否匹配
     * 这里没有加锁，最大的可能就是匹配不合理，问题不大
     */
    private fun checkCondition(temp: Player, player: Player): Boolean {
        return temp.level == player.level
    }
}