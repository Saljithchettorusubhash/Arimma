package com.example.secondassignmentgame
import com.example.secondassignmentgame.ArimaCharacter

interface ICharacterService {
    fun position(rowPos:Int,colPos:Int) : ArimaCharacter?
    fun selectCharacter(fromRowPos:Int,fromColPos: Int)
    fun moveCharacter(fromRowPos:Int,fromColPos: Int, toRowPos: Int, toColPos:Int)
    fun finishMovement()
    fun getTurn():String?
}