package com.example.secondassignmentgame

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.math.min

class GameStage(context : Context?, attrs : AttributeSet?) : View(context,attrs) {

    // We have to use 90% of the width to render the stage.
    private  final  val  scaleFactor = 0.90f

    private final var cellStartXPos = 20.0f
    private final var cellStartYPos = 200.0f
    private final var cellWidth = 130.0f

    private final val paint = Paint()


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        setResponsiveStage(canvas)
        drawBoard(canvas)
        drawTrap(canvas)
    }
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val minimumWidthScreen = min(widthMeasureSpec,heightMeasureSpec)
        setMeasuredDimension(minimumWidthScreen,minimumWidthScreen)
    }

    private fun setResponsiveStage(canvas: Canvas?) {
        if(canvas!=null){
            var stageSize = min(canvas.width,canvas.height) * scaleFactor
            cellWidth = stageSize / 8.0f
            cellStartXPos= (canvas.width - stageSize) / 2.0f
            cellStartYPos= (canvas.height - stageSize) / 2.0f
        }
    }

    private fun drawBoard(canvas: Canvas?){
        for(i in 0..7){
            for(j in 0..7){
                paint.color = if((i+j)%2==0) Color.YELLOW else Color.LTGRAY
                canvas?.drawRect(
                    cellStartXPos + i * cellWidth,
                    cellStartYPos +  j * cellWidth,
                    cellStartXPos + (i + 1) * cellWidth,
                    cellStartYPos + ( j + 1) * cellWidth,
                    paint
                )
            }
        }
    }

    private fun drawTrap(canvas: Canvas?) {
        val paint2 = Paint()
        paint2.color = Color.BLACK
        canvas?.drawRect(
            cellStartXPos +  3*  cellWidth,
            cellStartYPos + (1+2)*  cellWidth,
            cellStartXPos +  2*cellWidth,
            cellStartYPos +  2*cellWidth,
            paint2
        )
        canvas?.drawRect(
            cellStartXPos +  6*  cellWidth,
            cellStartYPos + (1+2)*  cellWidth,
            cellStartXPos +  (2+3)*cellWidth,
            cellStartYPos +  2*cellWidth,
            paint2
        )
        canvas?.drawRect(
            cellStartXPos +  3*  cellWidth,
            cellStartYPos + 5*  cellWidth,
            cellStartXPos +  2*cellWidth,
            cellStartYPos +  6*cellWidth,
            paint2
        )
        canvas?.drawRect(
            cellStartXPos +  6*  cellWidth,
            cellStartYPos + 5*  cellWidth,
            cellStartXPos +  (2+3)*cellWidth,
            cellStartYPos +  6*cellWidth,
            paint2
        )
    }
}