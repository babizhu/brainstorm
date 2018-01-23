package com.bbz.education.brainstorm.quiz.chatroom.quiz

import java.util.*

/**
 * @param answerIndex    the index of concrrect option in @param option
 */
data class Question(val question:String,
                    val level:Int,
                    val option: List<String>,
                    val answerIndex:Int ){


    private fun shuffle(){

        Collections.shuffle(option)

    }

    fun check(answerIndex: Int):Boolean{
        return answerIndex == this.answerIndex
    }
}
