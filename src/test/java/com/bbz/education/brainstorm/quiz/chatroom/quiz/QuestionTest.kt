package com.bbz.education.brainstorm.quiz.chatroom.quiz

import org.junit.Test

class QuestionTest {
    private val question: Question = Question("1+1=?", 1, listOf("1", "2", "3", "4", "5", "6"), 1)

    @Test
    fun shuffleTest() {
        println(question)
    }



}