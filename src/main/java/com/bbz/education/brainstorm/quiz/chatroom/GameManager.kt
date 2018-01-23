package com.bbz.education.brainstorm.quiz.chatroom

import com.bbz.education.brainstorm.quiz.chatroom.player.Player
import com.bbz.education.brainstorm.quiz.chatroom.player.PlayerManager

object GameManager{
    val playerManager = PlayerManager()

    fun login(name:String,password:String){
        var player = playerManager.login(name, password)

    }

    fun enterRoom(player:Player){

    }
}