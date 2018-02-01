package com.bbz.education.brainstorm.logic.quiz

import java.util.*

/**
 * @param answerIndex    the index of concrrect option in @param option
 */
data class Question(val question:String,
                    val option: List<String>,
                    val answerIndex:Int,
                    val level:Int
                    ){


    private fun shuffle(){

        Collections.shuffle(option)

    }

    fun check(answerIndex: Int):Boolean{
        return answerIndex == this.answerIndex
    }
}
