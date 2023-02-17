package com.example.secondassignmentgame

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.secondassignmentgame.ICharacterService
import kotlin.math.min

class GameStage(context : Context?,attrs : AttributeSet?) : View(context,attrs) {

    // We have to use 90% of the width to render the stage.
    private final val scaleFactor = 0.90f
    var possibleMovePoints : ArrayList<Point> ? = null

    private final var cellStartXPos = 20.0f
    private final var cellStartYPos = 200.0f
    private final var cellWidth = 130.0f

    private final val paint = Paint()

    private var isMoving: Boolean = false
    private var fromColPos: Int = -1
    private var fromRowPos: Int = -1

    var characterService: ICharacterService? = null

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        try{
            setResponsiveStage(canvas)
            drawBoard(canvas)
            drawTrap(canvas)
            drawCharacters(canvas)
        }catch (_:Exception){

        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return false
        try{
            removeHighlightIfExists()
            when(event.action){
                MotionEvent.ACTION_UP ->{
                    if(!isMoving){
                        fromColPos = ((event.x - cellStartXPos) / cellWidth).toInt()
                        fromRowPos=  ((event.y - cellStartYPos) / cellWidth).toInt()
                        var isMovable = characterService?.canMove(fromRowPos,fromColPos)
                        if(isMovable==false){
                            characterService?.getMessage()
                        }
                        var char = characterService?.position(fromRowPos,fromColPos)
                        // if already frozen unfreeze it.
                        if(char!=null) {
                            var isFreezedNow =
                                characterService?.freezeCharacter(fromRowPos, fromColPos)
                            var hasAdjFriend =characterService?.hasAdjacentFriend(fromRowPos,fromColPos)
                            if(hasAdjFriend== true){
                                isFreezedNow=false
                                CharacterModel.replaceCharacter(
                                    char!!,
                                    fromColPos,
                                    fromRowPos,
                                    char.player,
                                    char.strength,
                                    char.drawable,
                                    char.highlighted,
                                    false
                                )
                                char = characterService?.position(fromRowPos, fromColPos)
                            }
                            if (char?.isFrozen!! || isFreezedNow == true) {
                                CharacterModel.replaceCharacter(
                                    char!!,
                                    fromColPos,
                                    fromRowPos,
                                    char.player,
                                    char.strength,
                                    char.drawable,
                                    char.highlighted,
                                    true
                                )
                            } else {
                                CharacterModel.replaceCharacter(
                                    char!!,
                                    fromColPos,
                                    fromRowPos,
                                    char.player,
                                    char.strength,
                                    char.drawable,
                                    char.highlighted,
                                    false
                                )
                            }
                            // reset current character to take effect
                            char = characterService?.position(fromRowPos, fromColPos)

                            if (isMovable == true && !char?.isFrozen!!) {
                                characterService?.selectCharacter(fromRowPos, fromColPos)
                                possibleMovePoints =
                                    characterService?.hightlightPossibleMoves(
                                        fromRowPos,
                                        fromColPos
                                    )
                                highlightIt()
                                isMoving = true
                            } else if (char != null && char?.isFrozen!!) {
                                characterService?.getMessage()
                            }

                            invalidate()
                        }
                    }else{
                        var toColPos = ((event.x - cellStartXPos) / cellWidth).toInt()
                        var toRowPos= ((event.y - cellStartYPos) / cellWidth).toInt()
                        if(fromRowPos == toRowPos && fromColPos == toColPos)  {
                            isMoving=false
                            return false
                        }
                        var isSuccess = characterService?.moveCharacter(fromRowPos,fromColPos,toRowPos,toColPos)
                        var pushed = characterService?.pushCharactor(fromRowPos,fromColPos,toRowPos,toColPos)
                        if(pushed==true){
                            isSuccess=true
                            characterService?.getTurn()
                        }
                        if(!isSuccess!!){
                            isMoving=false
                            return false
                        }
                        if(characterService?.freezeCharacter(toRowPos,toColPos)==true){
                            if(characterService?.hasAdjacentFriend(toRowPos,toColPos)!=true) {
                                CharacterModel.setAsFrozen(toRowPos, toColPos)
                                isMoving = false
                            }
                        }
                        if(isSuccess==true){
                            possibleMovePoints = ArrayList<Point>()
                            isMoving =false
                        }
                        invalidate()
                    }
                }
            }
            return true
        }catch (_:Exception){
            return false
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val minimumWidthScreen = min(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(minimumWidthScreen, minimumWidthScreen)
    }

    private fun setResponsiveStage(canvas: Canvas?) {
        if (canvas != null) {
            var stageSize = min(canvas.width, canvas.height) * scaleFactor
            cellWidth = stageSize / 8.0f
            cellStartXPos = (canvas.width - stageSize) / 2.0f
            cellStartYPos = (canvas.height - stageSize) / 2.0f
        }
    }

    private fun drawBoard(canvas: Canvas?) {
        for (i in 0..7) {
            for (j in 0..7) {
                paint.color = if ((i + j) % 2 == 0) Color.YELLOW else Color.LTGRAY
                canvas?.drawRect(
                    cellStartXPos + i * cellWidth,
                    cellStartYPos + j * cellWidth,
                    cellStartXPos + (i + 1) * cellWidth,
                    cellStartYPos + (j + 1) * cellWidth,
                    paint
                )
            }
        }
    }

    private fun drawTrap(canvas: Canvas?) {
        val paint = Paint()
        paint.color = Color.BLACK
        canvas?.drawRect(
            cellStartXPos + 3 * cellWidth,
            cellStartYPos + (1 + 2) * cellWidth,
            cellStartXPos + 2 * cellWidth,
            cellStartYPos + 2 * cellWidth,
            paint
        )
        canvas?.drawRect(
            cellStartXPos + 6 * cellWidth,
            cellStartYPos + (1 + 2) * cellWidth,
            cellStartXPos + (2 + 3) * cellWidth,
            cellStartYPos + 2 * cellWidth,
            paint
        )
        canvas?.drawRect(
            cellStartXPos + 3 * cellWidth,
            cellStartYPos + 5 * cellWidth,
            cellStartXPos + 2 * cellWidth,
            cellStartYPos + 6 * cellWidth,
            paint
        )
        canvas?.drawRect(
            cellStartXPos + 6 * cellWidth,
            cellStartYPos + 5 * cellWidth,
            cellStartXPos + (2 + 3) * cellWidth,
            cellStartYPos + 6 * cellWidth,
            paint
        )
    }

    private fun drawCharacters(canvas: Canvas?) {
        for (rowPos in 0..7) {
            for (colPos in 0..7) {
                val char = characterService?.position(rowPos, colPos)
                if (char != null) {
                    val bmp = BitmapFactory.decodeResource(resources, char.drawable)
                    drawCharacterAtPos(canvas,bmp,rowPos,colPos, char.selected,char.highlighted,char.isFrozen)
                }
            }
        }
    }

    private fun drawCharacterAtPos(canvas: Canvas?, image: Bitmap, row:Int, col:Int, selected:Boolean,highlighted:Boolean,isFrozen:Boolean){
        try{
            var paintForBorder = Paint()
            if(selected) {
                //removeHighlightIfExists()
                paintForBorder.style = Paint.Style.STROKE
                paintForBorder.strokeWidth = 5.0f
                paintForBorder.color = Color.BLUE
                canvas?.drawRect(
                    cellStartXPos + fromColPos * cellWidth,
                    cellStartYPos + fromRowPos * cellWidth,
                    cellStartXPos + (fromColPos + 1) * cellWidth,
                    cellStartYPos + (fromRowPos + 1) * cellWidth,
                    paintForBorder
                )
                canvas?.drawBitmap(
                    image,
                    null,
                    RectF(
                        cellStartXPos + col * cellWidth,
                        cellStartYPos + row * cellWidth,
                        cellStartXPos + (col + 1) * cellWidth,
                        cellStartYPos + (row + 1) * cellWidth
                    ),
                    paint
                )
            }else {
                // removeHighlightIfExists()
                canvas?.drawBitmap(
                    image,
                    null,
                    RectF(
                        cellStartXPos + col * cellWidth,
                        cellStartYPos + row * cellWidth,
                        cellStartXPos + (col + 1) * cellWidth,
                        cellStartYPos + (row + 1) * cellWidth
                    ),
                    paint
                )
            }
            if(possibleMovePoints!=null) {
                for (char in possibleMovePoints!!) {
                    var movingCharacter = CharacterModel.position(char.x, char.y) ?: null
                    if (movingCharacter == null) {
                        paintForBorder.color = Color.parseColor("#607d8bb3")
                        canvas?.drawRect(
                            cellStartXPos + char.y * cellWidth,
                            cellStartYPos + char.x * cellWidth,
                            cellStartXPos + (char.y + 1) * cellWidth,
                            cellStartYPos + (char.x + 1) * cellWidth,
                            paintForBorder
                        )
                    }
                }
                canvas?.drawBitmap(
                    image,
                    null,
                    RectF(
                        cellStartXPos + col * cellWidth,
                        cellStartYPos + row * cellWidth,
                        cellStartXPos + (col + 1) * cellWidth,
                        cellStartYPos + (row + 1) * cellWidth
                    ),
                    paint
                )

            }
            if(isFrozen){
                paintForBorder.style = Paint.Style.STROKE
                paintForBorder.strokeWidth = 10.0f
                paintForBorder.color = Color.BLUE
                canvas?.drawBitmap(
                    image,
                    null,
                    RectF(
                        cellStartXPos + col * cellWidth,
                        cellStartYPos + row * cellWidth,
                        cellStartXPos + (col + 1) * cellWidth,
                        cellStartYPos + (row + 1) * cellWidth
                    ),
                    paintForBorder
                )
                canvas?.drawBitmap(
                    image,
                    null,
                    RectF(
                        cellStartXPos + col * cellWidth,
                        cellStartYPos + row * cellWidth,
                        cellStartXPos + (col + 1) * cellWidth,
                        cellStartYPos + (row + 1) * cellWidth
                    ),
                    paint
                )
            }
        }catch (_:Exception){}
    }
    private fun highlightIt(){
        try{
            if(CharacterModel.possibleMovePoints ==null)return;
            for (point in CharacterModel.possibleMovePoints!!) {
                var movingCharacter= CharacterModel.position(point.x, point.y) ?: null
                if( movingCharacter!=null){
                    CharacterModel.replaceCharacter(
                        movingCharacter, point.y, point.x, movingCharacter.player,
                        movingCharacter.strength, movingCharacter.drawable, true,movingCharacter.isFrozen
                    )
                }
            }
            invalidate()
        }catch (_:Exception){}
    }
    private fun removeHighlightIfExists(){
        try{
            if(CharacterModel.possibleMovePoints ==null)return;
            for (point in CharacterModel.possibleMovePoints!!) {
                var movingCharacter= CharacterModel.position(point.x, point.y) ?: null
                if( movingCharacter!=null){
                    CharacterModel.replaceCharacter(
                        movingCharacter, point.y, point.x, movingCharacter.player,
                        movingCharacter.strength, movingCharacter.drawable, false,movingCharacter.isFrozen
                    )
                }
            }
            invalidate()
        }catch (_:Exception){}
    }
}