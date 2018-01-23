package com.bbz.education.brainstorm.quiz.chatroom.room

import com.bbz.education.brainstorm.quiz.chatroom.player.Player
import com.bbz.education.brainstorm.quiz.chatroom.quiz.Question
import com.bbz.education.brainstorm.quiz.chatroom.quiz.QuestionSet
import com.bbz.education.brainstorm.quiz.chatroom.quiz.QuizCache
import com.bbz.education.brainstorm.quiz.chatroom.room.Room.Companion.PLAYER_NUMBER
import io.vertx.core.json.JsonObject

import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
import org.slf4j.LoggerFactory

class Round(val question: Question) {
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
        choose[player] = answer
        return answer == question.answerIndex
    }
}

data class MatchInfo(var point: Int, var lscs: Int)
class Room(level: Int, player1: Player,
           player2: Player) {

    private val matchInfoMap = HashMap<Player, MatchInfo>()
    //    private val playerList: List<Player>
    private val questionSet: QuestionSet = QuizCache.getQuestionSet(level)
    private val roundList = ArrayList<Round>()
    private lateinit var currentRound: Round

    init {
        matchInfoMap[player1] = MatchInfo(0, 0)
        matchInfoMap[player2] = MatchInfo(0, 0)
    }

    companion object {
        val logger = LoggerFactory.getLogger(this::class.java)!!
        /**
         * 本房间的挑战人数
         */
        const val PLAYER_NUMBER = 2
    }

    fun getCurrentQuestion() {
        val question = questionSet.getNextQuestion()
        currentRound = Round(question)
        roundList.add(currentRound)
        sendBroadcastMsg(buildQuestionJson())
    }

    private fun buildQuestionJson(): JsonObject {
        return json {
            obj(
                    "question" to currentRound.question.question,
                    "options" to currentRound.question.option.joinToString(",")
            )
        }
    }

    /**
     * 对房间内的用户广播
     */
    private fun sendBroadcastMsg(msg: JsonObject) {
//        logger.debug(msg.encode())
        println(msg)
        for (player in matchInfoMap.keys) {
//            player.webSocket.writeTextMessage(msg)
        }
    }

    /**
     * 用户选择答题
     */
    fun choose(player: Player, choose: Int) {
        if (currentRound.choose(choose, player)) {
            val matchInfo = matchInfoMap[player]
            matchInfo!!.lscs += 1
            matchInfo!!.point += calcPoint(player, true)
        }
        if (currentRound.isEnd()) {

            val result = JsonObject().put("choose", currentRound.question.answerIndex)

            for (entry in matchInfoMap) {
                result.put(entry.key.name,
                        JsonObject().put("choose", currentRound.choose[entry.key])
                                .put("point", entry.value.point)
                                .put("lscs", entry.value.lscs)
                )
            }
            sendBroadcastMsg(result)
        }

    }

    /**
     * 根据逻辑计算得分，根据规则有可能是负分
     * @param   isRight         答案是否正确
     */
    private fun calcPoint(player: Player, isRight: Boolean): Int {
        return if( isRight) {
            val beginTime = currentRound.beginTime

            200 - ((System.currentTimeMillis() - beginTime) / 1000).toInt() * 10 + matchInfoMap[player]!!.lscs * 10
        }else{
            0
        }
    }

}

fun main(args: Array<String>) {
    val player1 = Player("liulaoye")
    val player2 = Player("bbz")
    val room = Room(1, player1, player2)
    room.getCurrentQuestion()
    Thread.sleep(2000)
    println("等待答题中......")
    room.choose(player1, 0)
    room.choose(player2, 1)
    Thread.sleep(1000)
    println("下一题......")

    room.getCurrentQuestion()
    Thread.sleep(3000)
    println("等待答题中......")
    room.choose(player1, 2)
    room.choose(player2, 1)

//    room.getCurrentQuestion()

}