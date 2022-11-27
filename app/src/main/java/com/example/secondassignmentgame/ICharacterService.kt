package com.example.secondassignmentgame
import com.example.secondassignmentgame.ArimaCharacter

interface ICharacterService {
    fun position(rowPos:Int,colPos:Int) : ArimaCharacter?
}