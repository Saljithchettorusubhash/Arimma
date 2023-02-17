package com.example.secondassignmentgame

import android.graphics.Point
import com.example.secondassignmentgame.*
import android.util.Log
import java.lang.Math.abs



object CharacterModel {
    private val characters = mutableSetOf<ArimaCharacter>()

    var infoMessage : String = ""
    //Following two variables are keeping track of movement history
    var currentTurn : ArimaPlayer = ArimaPlayer.GOLD
    var currentSteps : Int = 0
    var possibleMovePoints : ArrayList<Point> ? = null

    var freezedChars :  ArrayList<ArimaCharacter> ? = null

    init{
        resetCharacters()
    }

    // Function to get the position of a selected character
    fun position(rowPos:Int,colPos:Int) : ArimaCharacter ?{
        try {
            var char = characters.first { it -> it.colPos == colPos && it.rowPos == rowPos }
            if(char!=null && char.isFrozen)  setMessage("Can't move a frozen character ")
            return  char
        }catch (err: java.lang.Exception){
            return null
        }
    }
    fun getTurn() : String {
        try{
            return "${currentTurn.toString()} [${4-currentSteps}]"
        }catch (_:Exception){
            return "TURN:GOLD[4]";
        }
    }

    fun setTurn(turn:ArimaPlayer)  {
        currentTurn = turn
    }

    fun setMessage(msg:String)  {
        infoMessage = msg
    }
    fun getMessage() : String {
        return infoMessage
    }


    private fun isVerticalMovement(fromRowPos:Int, fromColPos:Int, toRowPos:Int, toColPos:Int): Boolean{
        if(fromColPos != toColPos){
            if(fromRowPos==toRowPos) return false
            return true
        }
        return false;
    }
    private fun isMultiMove(fromRowPos:Int, fromColPos:Int, toRowPos:Int, toColPos:Int): Boolean{
        val movedRowCount = abs(fromRowPos -toRowPos)
        val movedColCount = abs(fromColPos -toColPos)
        if(fromRowPos == toRowPos){
            if(movedRowCount == 0 && movedColCount == 1) return false
        }else{
            if(movedRowCount == 1 && movedColCount == 0) return false
        }
        return true
    }

    private fun characterPresentInDestination(toRowPos:Int, toColPos:Int): Boolean{
        var char= position(toRowPos,toColPos) ?: null
        return char != null
    }

    private fun isRabbitMovePossible(fromRowPos:Int, fromColPos:Int,toRowPos:Int,toColPos:Int) : Boolean{
        if(fromRowPos == toRowPos && fromColPos == toColPos) return false
        val curCharacter = position(fromRowPos,fromColPos) ?: return false
        val characterAtDestination = position(toRowPos,toColPos) ?:null
        if(curCharacter.strength == PlayerStrength.RABBIT){
            if(curCharacter.player == ArimaPlayer.GOLD && fromRowPos<toRowPos){
                setMessage("Rabbits can't move backward")
                return false;
            }else if(curCharacter.player == ArimaPlayer.SILVER && fromRowPos>toRowPos){
                setMessage("Rabbits can't move backward")
                return false;
            }
            if(characterAtDestination!=null){
                setMessage("Movement possible only to empty space")
                return false;
            }
            if(fromColPos == toColPos || fromRowPos == toRowPos){
                return true
            }else{
                setMessage("Movement possible only to adjacent sides")
            }
        }
        return false
    }

    fun resetCharacters() {
        try{
            characters.removeAll(characters)
            //setting rabbits initial position
            for(i in 0..7){ // rows
                characters.add(ArimaCharacter(i,0,ArimaPlayer.SILVER,PlayerStrength.RABBIT, R.drawable.srabit,false,false,false,false))
                characters.add(ArimaCharacter(i,7,ArimaPlayer.GOLD,PlayerStrength.RABBIT, R.drawable.rabit,false,false,false,false))
            }
            //setting cats initial position
            characters.add(ArimaCharacter(0,1,ArimaPlayer.SILVER,PlayerStrength.CAT, R.drawable.scat,false,true,false,false))
            characters.add(ArimaCharacter(7,1,ArimaPlayer.SILVER,PlayerStrength.CAT, R.drawable.scat,false,true,false,false))
            characters.add(ArimaCharacter(0,6,ArimaPlayer.GOLD,PlayerStrength.CAT, R.drawable.cat,false,true,false,false))
            characters.add(ArimaCharacter(7,6,ArimaPlayer.GOLD,PlayerStrength.CAT, R.drawable.cat,false,true,false,false))
            //setting dogs initial position
            characters.add(ArimaCharacter(1,1,ArimaPlayer.SILVER,PlayerStrength.DOG, R.drawable.sdog,false,true,false,false))
            characters.add(ArimaCharacter(6,1,ArimaPlayer.SILVER,PlayerStrength.DOG, R.drawable.sdog,false,true,false,false))
            characters.add(ArimaCharacter(1,6,ArimaPlayer.GOLD,PlayerStrength.DOG, R.drawable.dog,false,true,false,false))
            characters.add(ArimaCharacter(6,6,ArimaPlayer.GOLD,PlayerStrength.DOG, R.drawable.dog,false,true,false,false))
            //setting horses initial position
            characters.add(ArimaCharacter(2,1,ArimaPlayer.SILVER,PlayerStrength.HORSE, R.drawable.shorse,false,true,false,false))
            characters.add(ArimaCharacter(5,1,ArimaPlayer.SILVER,PlayerStrength.HORSE, R.drawable.shorse,false,true,false,false))
            characters.add(ArimaCharacter(2,6,ArimaPlayer.GOLD,PlayerStrength.HORSE, R.drawable.horse,false,true,false,false))
            characters.add(ArimaCharacter(5,6,ArimaPlayer.GOLD,PlayerStrength.HORSE, R.drawable.horse,false,true,false,false))

            //setting camels initial position
            characters.add(ArimaCharacter(3,1,ArimaPlayer.SILVER,PlayerStrength.CAMEL, R.drawable.scamel,false,true,false,false))
            characters.add(ArimaCharacter(3,6,ArimaPlayer.GOLD,PlayerStrength.CAMEL, R.drawable.camel,false,true,false,false))

            //setting elephant initial position
            characters.add(ArimaCharacter(4,1,ArimaPlayer.SILVER,PlayerStrength.ELEPHANT, R.drawable.selephant,false,true,false,false))
            characters.add(ArimaCharacter(4,6,ArimaPlayer.GOLD,PlayerStrength.ELEPHANT, R.drawable.elephant,false,true,false,false))
            possibleMovePoints = ArrayList<Point>()
            currentTurn = ArimaPlayer.GOLD;
            currentSteps = 0
            setMessage("")
            setTurn(ArimaPlayer.GOLD)
            removeHighlightIfExists()
        }catch (_:Exception){}
    }

    fun moveCharacter(sourceRowPos: Int,sourceColPos: Int, destinationRowPos: Int, destinationColPos:Int):Boolean{
        try{
            if(sourceRowPos==destinationRowPos && sourceColPos == destinationColPos) return false
            var movingCharacter = position(sourceRowPos,sourceColPos) ?: return false
            if(movingCharacter.isFrozen){
                setMessage("Can't move a frozen character")
                return false
            }
            if(isVerticalMovement(sourceRowPos, sourceColPos,destinationRowPos,destinationColPos)){
                setMessage("Can't allow movements vertically")
                return false
            }
            if(isMultiMove(sourceRowPos, sourceColPos,destinationRowPos,destinationColPos)){
                setMessage("One move at a time")
                return false
            }
            if(characterPresentInDestination(destinationRowPos,destinationColPos)){
                setMessage("Can't move to already occupied space")
                return false
            }
            if(movingCharacter.strength == PlayerStrength.RABBIT){
                var rabbitMovePossibility = isRabbitMovePossible(sourceRowPos,sourceColPos,destinationRowPos,destinationColPos)
                if(!rabbitMovePossibility) return false
            }
            if(currentTurn==ArimaPlayer.GOLD && movingCharacter.player != ArimaPlayer.GOLD ){
                setMessage("Wait, Gold's Turn")
                return false
            }
            if(currentTurn == ArimaPlayer.SILVER && movingCharacter.player!=ArimaPlayer.SILVER) {
                setMessage("Wait, Silver's Turn")
                return false
            }
            if(currentTurn==ArimaPlayer.GOLD && currentSteps<=4){
                if(movingCharacter.player==ArimaPlayer.SILVER) return false
                Log.d("Moving: $currentSteps",movingCharacter.player.toString())
                replaceCharacter(movingCharacter,destinationColPos,destinationRowPos,movingCharacter.player
                    ,movingCharacter.strength,movingCharacter.drawable)
                currentSteps++;
                if(currentSteps>3) {
                    finishMovement()
                };
                return true
            }else{
                if(movingCharacter.player==ArimaPlayer.GOLD) return false
                Log.d("Moving: $currentSteps",movingCharacter.player.toString())
                replaceCharacter(movingCharacter,destinationColPos,destinationRowPos,movingCharacter.player
                    ,movingCharacter.strength,movingCharacter.drawable)
                currentSteps++;
                if(currentSteps>3) {
                    finishMovement()
                };
                return true
            }
        }catch (_:Exception){
            return  false;
        }
    }
    fun replaceCharacter(curCharacter: ArimaCharacter, toCol:Int, toRow:Int, player:ArimaPlayer,
                         strength: PlayerStrength, drawable:Int, highlighted:Boolean=false, frozen:Boolean=false){
        try{
            var isRabit = curCharacter.strength==PlayerStrength.RABBIT
            characters.remove(curCharacter)
            characters.add(ArimaCharacter(toCol,toRow,player,strength,drawable,curCharacter.selected,isRabit,frozen,highlighted))
        }catch (_:Exception){}
    }
    fun selectCharacter(fromRowPos:Int,fromColPos: Int){
        try{
            var movingCharacter = position(fromRowPos,fromColPos) ?: null
            if(movingCharacter!=null) {
                characters.remove(movingCharacter)
                characters.add(
                    ArimaCharacter(
                        fromColPos,
                        fromRowPos,
                        movingCharacter.player,
                        movingCharacter.strength,
                        movingCharacter.drawable,
                        true,
                        movingCharacter.canMoveBackward,
                        movingCharacter.isFrozen,
                        false
                    )
                )
            }
        }catch (_:Exception){

        }
    }
    fun finishMovement(){
        try{
            if(currentSteps<=0){
                setMessage("You have to make at least one move")
            }else {
                currentSteps = 0;
                if (currentTurn == ArimaPlayer.GOLD) setTurn(ArimaPlayer.SILVER) else setTurn(
                    ArimaPlayer.GOLD
                )
            }
        }
        catch (_:Exception){}
    }
    fun hightlightPossibleMoves(fromRowPos:Int, fromColPos: Int): ArrayList<Point> ?{
        try{
            removeHighlightIfExists()
            possibleMovePoints?.clear()
            possibleMovePoints = ArrayList<Point>()
            if(fromRowPos==7){
                when (fromColPos) {
                    in 1..6 -> {
                        // all adjacent sides are available to move
                        possibleMovePoints?.add(Point(fromRowPos, kotlin.math.abs(fromColPos + 1)))
                        possibleMovePoints?.add(Point(fromRowPos, kotlin.math.abs(fromColPos - 1)))
                        possibleMovePoints?.add(Point(kotlin.math.abs(fromRowPos + 1),fromColPos ))
                        possibleMovePoints?.add(Point(kotlin.math.abs(fromRowPos - 1),fromColPos ))
                    }
                    0 -> {
                        possibleMovePoints?.add(Point(Point(kotlin.math.abs(fromRowPos - 1), fromColPos)))
                        possibleMovePoints?.add(Point(fromRowPos ,  kotlin.math.abs(fromColPos + 1)))
                    }
                    else -> {
                        possibleMovePoints?.add(Point(Point(kotlin.math.abs(fromRowPos - 1), fromColPos)))
                        possibleMovePoints?.add(Point(fromRowPos ,  kotlin.math.abs(fromColPos - 1)))
                    }
                }
            }else if(fromRowPos==0){
                when (fromColPos) {
                    in 1..6 -> {
                        // all adjacent sides are available to move
                        possibleMovePoints?.add(Point(fromRowPos, kotlin.math.abs(fromColPos + 1)))
                        possibleMovePoints?.add(Point(fromRowPos, kotlin.math.abs(fromColPos - 1)))
                        possibleMovePoints?.add(Point(kotlin.math.abs(fromRowPos + 1),fromColPos ))
                        possibleMovePoints?.add(Point(kotlin.math.abs(fromRowPos - 1),fromColPos ))
                    }
                    0 -> {
                        possibleMovePoints?.add(Point(Point(kotlin.math.abs(fromRowPos + 1), fromColPos)))
                        possibleMovePoints?.add(Point(fromRowPos ,  kotlin.math.abs(fromColPos + 1)))
                        //highlightIt()
                    }
                    else -> {
                        possibleMovePoints?.add(Point(Point(kotlin.math.abs(fromRowPos + 1), fromColPos)))
                        possibleMovePoints?.add(Point(fromRowPos ,  kotlin.math.abs(fromColPos - 1)))
                    }
                }
            }else{
                when (fromColPos) {
                    in 1..6 -> {
                        // all adjacent sides are available to move
                        possibleMovePoints?.add(Point(fromRowPos, kotlin.math.abs(fromColPos + 1)))
                        possibleMovePoints?.add(Point(fromRowPos, kotlin.math.abs(fromColPos - 1)))
                        possibleMovePoints?.add(Point(kotlin.math.abs(fromRowPos + 1),fromColPos ))
                        possibleMovePoints?.add(Point(kotlin.math.abs(fromRowPos - 1),fromColPos ))
                    }
                    0 -> {
                        possibleMovePoints?.add(Point(Point(kotlin.math.abs(fromRowPos + 1), fromColPos)))
                        possibleMovePoints?.add(Point(fromRowPos ,  kotlin.math.abs(fromColPos + 1)))
                    }
                    else -> {
                        possibleMovePoints?.add(Point(Point(kotlin.math.abs(fromRowPos + 1), fromColPos)))
                        possibleMovePoints?.add(Point(fromRowPos ,  kotlin.math.abs(fromColPos - 1)))
                    }
                }
            }
            return possibleMovePoints;
        }catch (_:Exception){
            return possibleMovePoints;
        }
    }

    private fun removeHighlightIfExists(){
        try{
            if(possibleMovePoints==null)return;
            for (point in possibleMovePoints!!) {
                var movingCharacter= position(point.x,point.y) ?: null
                if( movingCharacter!=null){
                    replaceCharacter(movingCharacter,point.y,point.x,movingCharacter.player,
                        movingCharacter.strength,movingCharacter.drawable,false, movingCharacter.isFrozen
                    )
                }
            }
        }catch (_:Exception){
        }
    }



    fun canMove(rowPos: Int,colPos: Int) : Boolean{
        try{
            var movingCharacter = position(rowPos,colPos) ?: null
            var canMove = movingCharacter?.player == currentTurn && currentSteps<=4
            return  canMove;

        }catch (_:Exception){
            return false
        }
    }
    private fun isSamePlayer(myRow: Int,myCol: Int, adjucentRow: Int, adjucentCol: Int): Boolean {
        try{
            var me = position(myRow,myCol) ?:null
            var adjucent = position(adjucentRow,adjucentCol) ?:null
            return me?.player == adjucent?.player
        }catch (_:Exception){
            return false
        }
    }
    fun freezeCharacter(rowPos: Int,colPos: Int):Boolean{
        return isSmallerCharacter(rowPos,colPos)
    }
    private fun isSmallerCharacter(rowPos: Int,colPos: Int) :Boolean{
        try{
            var leftSide = position(rowPos,colPos-1)
            var rightSide = position(rowPos,colPos+1)
            var topSide = position(rowPos-1,colPos)
            var bottomSode = position(rowPos+1,colPos)
            if(leftSide!=null){
                var me = position(rowPos,colPos) ?:null?: return false
                if(me?.strength?.ordinal!!<leftSide.strength.ordinal && me?.player!=leftSide.player) {
                    return true
                }
            }else if(rightSide!=null ){
                var me: ArimaCharacter? = position(rowPos,colPos) ?:null ?: return false
                if(me?.strength?.ordinal!!<rightSide.strength.ordinal && me?.player!=rightSide.player) {
                    return true
                }
            }else if(topSide!=null){
                var me = position(rowPos,colPos) ?:null?: return false
                if(me?.strength?.ordinal!!<topSide.strength.ordinal && me?.player!=topSide.player) {
                    return true
                }
            }else if(bottomSode!=null){
                var me = position(rowPos,colPos) ?:null?: return false
                if(me?.strength?.ordinal!!<bottomSode.strength.ordinal && me?.player!=bottomSode.player) {
                    return true
                }
            }
            return false
        }catch (_:Exception){
            return false
        }
    }
    fun isLargerCharacter(sourceRowPos: Int,sourceColPos: Int,destinationRowPos: Int,destinationColPos: Int) :Boolean{
        try{
            var leftSide = position(destinationRowPos,sourceColPos-1)
            var rightSide = position(destinationRowPos,sourceColPos+1)
            var topSide = position(destinationRowPos,sourceColPos)
            var bottomSode = position(destinationRowPos,sourceColPos)
            if(leftSide!=null){
                var me = position(sourceRowPos,sourceColPos) ?:null?: return false
                if(me?.strength?.ordinal!!>leftSide.strength.ordinal && me?.player!=leftSide.player) {
                    return true
                }
            }else if(rightSide!=null ){
                var me: ArimaCharacter? = position(sourceRowPos,sourceColPos) ?:null ?: return false
                if(me?.strength?.ordinal!!>rightSide.strength.ordinal && me?.player!=rightSide.player) {
                    return true
                }
            }else if(topSide!=null){
                var me = position(sourceRowPos,sourceColPos) ?:null?: return false
                if(me?.strength?.ordinal!!>topSide.strength.ordinal && me?.player!=topSide.player) {
                    return true
                }
            }else if(bottomSode!=null){
                var me = position(sourceRowPos,sourceColPos) ?:null?: return false
                if(me?.strength?.ordinal!!>bottomSode.strength.ordinal && me?.player!=bottomSode.player) {
                    return true
                }
            }
            return false
        }catch (_:Exception){
            return false
        }
    }
    fun hasAdjacentFriend(rowPos: Int,colPos: Int) :Boolean
    {
        val leftSide = position(rowPos,colPos-1)
        val rightSide = position(rowPos,colPos+1)
        val topSide = position(rowPos-1,colPos)
        val bottomSide = position(rowPos+1,colPos)
        var me = position(rowPos,colPos)
        if(me!=null)
            return (leftSide!=null && me.player==leftSide.player) ||  (rightSide!=null && me.player==rightSide.player) ||
                    (topSide!=null && me.player==topSide.player) ||
                    (bottomSide!=null && me.player==bottomSide.player)
        else return false

    }
    fun getAdjucentEmptySpace(rowPos: Int,colPos: Int) :Point?{
        try{
            val leftSide = position(rowPos,colPos-1) ?: null
            val rightSide = position(rowPos,colPos+2)
            val topSide = position(rowPos-1,colPos)?: null
            val bottomSode = position(rowPos+1,colPos)?: null
            if(leftSide==null){
                if(colPos<=0){
                    if(position(rowPos-1,0)==null)
                        return Point(rowPos-1,0)
                    else if(position(rowPos+1,0)==null)
                        return  Point(rowPos+1,0)
                }
                return Point(rowPos,colPos-1)
            }else if(rightSide==null ){
                if(colPos>=7){
                    if(position(rowPos-1,7)==null)
                        return Point(rowPos-1,7)
                    else if(position(rowPos+1,7)==null)
                        return  Point(rowPos+1,7)
                }
                return Point(rowPos,colPos+1)
            }else if(topSide==null){
                if(rowPos<=0){
                    if(position(0,colPos+1)==null)
                        return Point(0,colPos+1)
                    else if(position(0,colPos-1)==null)
                        return  Point(0,colPos-1)
                }
                return Point(rowPos-1,colPos)
            }else if(bottomSode==null && rowPos<7){
                if(rowPos>=7){
                    if(position(7,colPos+1)==null)
                        return Point(7,colPos+1)
                    else if(position(7,colPos-1)==null)
                        return  Point(7,colPos-1)
                }
                return Point(rowPos-1,colPos)
            }
            return null
        }catch (_:Exception){
            return null
        }
    }
    fun isFreezed(rowPos: Int,colPos: Int) :Boolean{
        try {
            var item =
                freezedChars?.first { it -> it.colPos == colPos && it.rowPos == rowPos } ?: null
            if (item != null) {
                setMessage("Can't move a frozen character")
                return true
            }
            return false
        }catch (e:java.lang.Exception){
            return false
        }
    }
    fun setAsFrozen(rowPos:Int,colPos:Int){
        try{
            var char = position(rowPos,colPos)?:null
            if(char!=null)
                replaceCharacter(char,char.colPos,char.rowPos,char.player,char.strength,char.drawable,false,true)
        }
        catch (_:Exception){

        }
    }

    fun pushCharactor(rowPos:Int,colPos:Int,toRow:Int,toCol:Int ):Boolean{
        if(isLargerCharacter(rowPos,colPos, toRow,toCol)){
            var p= getAdjucentEmptySpace(toRow,toCol)
            if(p!=null){
                var char = position(p.x,p.y) ?:null
                if(char==null)
                    if(currentSteps>=2) {
                        position(toRow,toCol)?.let {
                            replaceCharacter(
                                it,
                                p.y,
                                p.x,
                                it.player,
                                it.strength,
                                it.drawable,
                                it.highlighted,
                                it.isFrozen
                            )
                        }
                        currentSteps+=2;
                        return true
                    }else{
                        setMessage("You doesn't have that much moves")
                        return false
                    }

            }
            return false
        }
        return false
    }
}