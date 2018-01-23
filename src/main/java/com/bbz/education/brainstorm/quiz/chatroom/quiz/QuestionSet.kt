package com.bbz.education.brainstorm.quiz.chatroom.quiz

class QuestionSet(private val questions:List<Question>) {

    private var currentQuestionIndex:Int = 0
    init {
        for (question in questions) {
            question.option
        }
    }

    fun getNextQuestion(): Question {
        return questions[currentQuestionIndex++]
    }

}