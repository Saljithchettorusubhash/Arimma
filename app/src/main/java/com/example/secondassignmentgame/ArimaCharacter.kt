package com.example.secondassignmentgame

data class ArimaCharacter(
    val colPos:Int, val rowPos:Int, val player:ArimaPlayer,
    val  strength: PlayerStrength, val drawable: Int, val selected:Boolean = false) {
    override fun toString(): String {
        return strength.toString()+"-"+ player.toString()+"-"+ colPos.toString()+"-"+rowPos.toString();
    }
}