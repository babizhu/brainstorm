package com.bbz.education.brainstorm.logic.match

import com.bbz.education.brainstorm.logic.quiz.Question
import com.bbz.education.brainstorm.quiz.chatroom.player.Player
import org.slf4j.LoggerFactory

class Round(val question: Question) {
    companion object {
        val logger = LoggerFactory.getLogger(this::class.java)!!
        /**
         * 本房间的挑战人数
         */
        const val PLAYER_NUMBER = 2
    }

    val choose = HashMap<Player, Int>()
    /**
     * 本回合开始的时间（毫秒）
     */
    val beginTime = System.currentTimeMillis()

    /**
     * 本回合是否结束
     */
    fun isEnd(): Boolean {
        return choose.size == PLAYER_NUMBER
    }

    fun choose(answer: Int, player: Player): Boolean {
        if (choose.containsKey(player)) {
            throw RuntimeException("已经答过题了")
        }
        choose[player] = answer
        return answer == question.answerIndex
    }
}