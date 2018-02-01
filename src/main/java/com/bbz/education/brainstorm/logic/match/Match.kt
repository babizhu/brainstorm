package com.bbz.education.brainstorm.logic.match

import com.bbz.education.brainstorm.quiz.chatroom.player.Player
import com.bbz.education.brainstorm.logic.quiz.QuestionSet
import com.bbz.education.brainstorm.logic.quiz.QuizCache
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
import org.slf4j.LoggerFactory

data class PlayerMatchInfo(var point: Int, var lscs: Int)


class Match(level: Int, players: List<Player>) {

    private val matchInfo = HashMap<Player, PlayerMatchInfo>()
    //    private val playerList: List<Player>
    private val questionSet: QuestionSet = QuizCache.getQuestionSet(level)
    private val roundList = ArrayList<Round>()
    private lateinit var currentRound: Round


    init {
        for (player in players) {
            matchInfo[player] = PlayerMatchInfo(0, 0)
        }
    }

    companion object {
        val logger = LoggerFactory.getLogger(this::class.java)!!

    }

    fun sendCurrentQuestion() {
        val question = questionSet.getNextQuestion()
        currentRound = Round(question)
        roundList.add(currentRound)
        sendBroadcast(buildQuestionJson())

    }

    fun end(){
        saveToDB()
    }

    private fun saveToDB() {

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
    private fun sendBroadcast(msg: JsonObject) {
//        logger.debug(msg.encode())

        for (player in matchInfo.keys) {
            try {
                player.socket.writeTextMessage(msg.encode())
            } catch (exception: Exception) {
                logger.error("${player.name} 抛出异常：$exception.message")

            }
        }
    }

    /**
     * 用户选择答题
     */
    fun choose(player: Player, choose: Int) {
        if (currentRound.choose(choose, player)) {
            val matchInfo = matchInfo[player]
            matchInfo!!.lscs += 1
            matchInfo!!.point += calcPoint(player, true)
        }
        if (currentRound.isEnd()) {

            val result = JsonObject().put("answer", currentRound.question.answerIndex)

            for (entry in matchInfo) {
                result.put(entry.key.name,
                        JsonObject().put("choose", currentRound.choose[entry.key])
                                .put("point", entry.value.point)
                                .put("lscs", entry.value.lscs)
                )
            }
            sendBroadcast(result)
            //推送下一道题
            if (questionSet.hasNext()) {
                sendCurrentQuestion()
            } else {

                var end = JsonObject()
                for (entry in matchInfo) {
                    end.put(entry.key.name, entry.value.point)
                }

                sendBroadcast(end)
            }
        }

    }

    /**
     * 根据逻辑计算得分，根据规则有可能是负分
     * @param   isRight         答案是否正确
     */
    private fun calcPoint(player: Player, isRight: Boolean): Int {
        return if (isRight) {
            val beginTime = currentRound.beginTime

            200 - ((System.currentTimeMillis() - beginTime) / 1000).toInt() * 10 + matchInfo[player]!!.lscs * 10
        } else {
            0
        }
    }

}

fun main(args: Array<String>) {
//    val player1 = Player("liulaoye", socket)
//    val player2 = Player("bbz", socket)
//    val match = Match(1, player1, player2)
//    match.sendCurrentQuestion()
//    Thread.sleep(2000)
//    println("等待答题中......")
//    match.choose(player1, 0)
//    match.choose(player2, 1)
//    Thread.sleep(1000)
//    println("下一题......")
//
//    match.sendCurrentQuestion()
//    Thread.sleep(3000)
//    println("等待答题中......")
//    match.choose(player1, 2)
//    match.choose(player2, 1)
//
//    match.sendCurrentQuestion()

}