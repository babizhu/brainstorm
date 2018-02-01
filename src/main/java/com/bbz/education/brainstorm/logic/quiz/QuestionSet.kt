package com.bbz.education.brainstorm.logic.quiz

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

    /**
     * 是否还有下一道题
     */
    fun hasNext():Boolean{
        return currentQuestionIndex < questions.size
    }

}