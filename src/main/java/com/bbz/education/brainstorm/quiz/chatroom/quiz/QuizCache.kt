package com.bbz.education.brainstorm.quiz.chatroom.quiz

import java.io.File
import java.nio.charset.Charset
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * 负责从数据库或其他存储设备获取所有的题目，保存到内存中
 *
 */
object QuizCache {

    private val historyQuestionsMap: Map<Int, MutableList<Question>>//<level,List<Question>>
    private val mathQuestionsMap: Map<Int, MutableList<Question>>
    private val chineseQuestionsMap: Map<Int, MutableList<Question>>
    private val random: Random = Random()

    init {
        historyQuestionsMap = HashMap()
        val historyList = ArrayList<Question>()
//        historyList.add(Question("第一次鸦片战争始于哪一年", 1, listOf("1841", "1941", "1840", "1852", "1842", "1843"), 2))
        historyQuestionsMap.put(1, historyList)

        mathQuestionsMap = HashMap()
        val mathList = ArrayList<Question>()
//        mathList.add(Question("三角形的内角只和是多少度", 1, listOf("180", "360", "720", "0"), 0))
        mathQuestionsMap.put(1, mathList)

        chineseQuestionsMap = HashMap()
        val chineseList = ArrayList<Question>()
//        chineseList.add(Question("《将进酒》的作者是谁", 1, listOf("陶渊明", "杜甫", "白居易", "李白"), 3))
        chineseQuestionsMap.put(1, chineseList)

        val questionDir = File("""resource/questions""")
        val fileTree: FileTreeWalk = questionDir.walk()
        fileTree.maxDepth(1)
                .filter { it.isFile }
                .filter { it.extension == "ini" }
                .forEach {
                    val map = when (it.nameWithoutExtension) {
                        "history" -> historyQuestionsMap
                        "chinese" -> chineseQuestionsMap
                        "math" -> mathQuestionsMap
                        else -> HashMap()
                    }

                    for (line in it.readLines(Charset.forName("UTF-8"))) {
                        if (!line.startsWith("#")) {

                            var split = line.split(" ")
                            val question = split[0]
                            val options = split[1].split(",").toList()
                            val answer = Integer.parseInt(split[2])
                            val level = Integer.parseInt(split[3])
                            var list = map[level]
                            if (list == null) {
                                list = ArrayList()
                            }
                            list.add(Question(question, options, answer, level))
                            map[level] = list
                        }
                    }

                }

    }

    fun getQuestionSet(level: Int): QuestionSet {
        val mathQuestion = getRandomQuestion(mathQuestionsMap[level])
        val historyQuestion = getRandomQuestion(historyQuestionsMap[level])
        val chineseQuestion = getRandomQuestion(chineseQuestionsMap[level])
        val questionList = ArrayList<Question>()
        questionList.add(mathQuestion)
        questionList.add(historyQuestion)
        questionList.add(chineseQuestion)
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

fun main(args: Array<String>) {
    QuizCache.getQuestionSet(1)
    print("a")
}