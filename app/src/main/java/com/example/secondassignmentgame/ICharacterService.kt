package com.example.secondassignmentgame
import android.graphics.Point
import com.example.secondassignmentgame.ArimaCharacter
import com.example.secondassignmentgame.ArimaPlayer

interface ICharacterService {
    fun position(rowPos:Int,colPos:Int) : ArimaCharacter?
    fun selectCharacter(fromRowPos:Int,fromColPos: Int)
    fun moveCharacter(fromRowPos:Int,fromColPos: Int, toRowPos: Int, toColPos:Int) :Boolean
    fun finishMovement()
    fun getTurn():String?
    fun getMessage():String?
    fun hightlightPossibleMoves(fromRowPos:Int, fromColPos: Int) : ArrayList<Point> ?
    fun canMove(rowPos: Int,colPos: Int) : Boolean
    fun freezeCharacter(rowPos: Int,colPos: Int) :Boolean
    fun isFreezed(rowPos: Int,colPos: Int) :Boolean
    fun setAsFrozen(rowPos:Int,colPos:Int)
    fun isLargerCharacter(sourceRowPos: Int,sourceColPos: Int,destinationRowPos: Int,destinationColPos: Int) :Boolean
    fun pushCharactor(rowPos:Int,colPos:Int,destinationRowPos: Int,destinationColPos: Int ):Boolean
    fun hasAdjacentFriend(rowPos: Int, colPos: Int):Boolean
}