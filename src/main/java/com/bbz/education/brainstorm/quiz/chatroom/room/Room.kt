package com.bbz.education.brainstorm.quiz.chatroom.room

import com.bbz.education.brainstorm.quiz.chatroom.player.Player
import com.bbz.education.brainstorm.quiz.chatroom.quiz.Question
import com.bbz.education.brainstorm.quiz.chatroom.quiz.QuestionSet
import com.bbz.education.brainstorm.quiz.chatroom.quiz.QuizCache

class Room(level: Int, player1: Player,
           player2: Player) {

    private val playerList: List<Player>
    private val questionSet: QuestionSet = QuizCache.getQuestionSet(level)
    private lateinit var currentQuestion: Question
    private lateinit var beginWithMs: Long

    init {
        playerList = ArrayList()
    }


    fun sendQuestion() {
        currentQuestion = questionSet.getNextQuestion()
        beginWithMs = System.currentTimeMillis()

        for (player in playerList) {
            player.sendQuestion(currentQuestion)
        }
    }

    fun choose(player: Player,answer:Int) {
        if(currentQuestion.check(answer)){
            player.addPoint(calcPoint())
        }
        if(chooseAll()){

        }
    }

    /**
     * 检测两个玩家是否都已经答题了
     */
    private fun chooseAll(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * 计算得分
     */
    fun calcPoint() :Int{
        return ((System.currentTimeMillis() - beginWithMs)/1000).toInt()
    }

}