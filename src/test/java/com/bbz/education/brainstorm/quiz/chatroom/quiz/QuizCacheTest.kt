package com.bbz.education.brainstorm.quiz.chatroom.quiz

import org.junit.Test

class QuizCacheTest {

    @Test
    fun getQuestionSet() {
        var questionSet = QuizCache.getQuestionSet(1)
        println(questionSet)
    }
}