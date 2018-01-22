package com.bbz.education.brainstorm.quiz.chatroom.quiz

import java.util.*

/**
 * 负责和数据库获取所有的题目，保存到内存中
 *
 */
object QuizCache {

    private val historyQuestionsMap: Map<Int, List<Question>>
    private val mathQuestionsMap: Map<Int, List<Question>>
    private val random: Random = Random()

    init {
        historyQuestionsMap = HashMap()
        val historyList = ArrayList<Question>()
        var question = Question("第一次鸦片战争始于哪一年", 1, listOf("1841", "1941", "1840", "1852", "1842", "1843"), 3)
        historyList.add(question)
        historyQuestionsMap.put(1, historyList)

        mathQuestionsMap = HashMap()
        val mathList = ArrayList<Question>()
        mathList.add(Question("三角形的内角只和是多少度", 1, listOf("180", "360", "720", "0"), 0))
        mathQuestionsMap.put(1, mathList)
    }

    fun getQuestionSet(level: Int): QuestionSet {
        val mathQuestion = getRandomQuestion(mathQuestionsMap[level])
        val historyQuestion = getRandomQuestion(historyQuestionsMap[level])
        val questionList = ArrayList<Question>()
        questionList.add(mathQuestion)
        questionList.add(historyQuestion)
        return QuestionSet(questionList)

    }

    private fun getRandomQuestion(list: List<Question>?): Question {
//       return list?.get(random.nextInt(list.size)) : null
        if (list != null) {
            return list[random.nextInt(list.size)]
        }
        throw IllegalArgumentException("name expected")
    }

}