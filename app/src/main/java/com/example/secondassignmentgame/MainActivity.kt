package com.example.secondassignmentgame

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.provider.CalendarContract
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.secondassignmentgame.CharacterModel
import com.example.secondassignmentgame.ArimaCharacter

class MainActivity : Activity() , ICharacterService {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val stage = findViewById<GameStage>(R.id.stage)
        // Connect to consume the service implementation
        // to use its service in the view
        findViewById<GameStage>(R.id.stage).characterService = this
        findViewById<Button>(R.id.btn_reset).setOnClickListener(View.OnClickListener {
            CharacterModel.resetCharacters()
            finishMovement()
            CharacterModel.setTurn(ArimaPlayer.GOLD)
            getMessage()
            getTurn()
            findViewById<Button>(R.id.btn_silver_finish).isEnabled = false
            findViewById<GameStage>(R.id.stage).invalidate()
        })
        findViewById<Button>(R.id.btn_finish).setOnClickListener(View.OnClickListener {
            finishMovement()
        })
        findViewById<Button>(R.id.btn_silver_finish).setOnClickListener(View.OnClickListener {
            finishMovement()
        })
        getTurn()
    }
    override fun position(rowPos: Int, colPos: Int): ArimaCharacter? {
        return CharacterModel.position(rowPos, colPos)
    }

    override fun selectCharacter(fromRowPos: Int, fromColPos: Int) {
        CharacterModel.selectCharacter(fromRowPos,fromColPos)
        findViewById<GameStage>(R.id.stage).invalidate()
    }

    override fun moveCharacter(fromRowPos: Int, fromColPos: Int, toRowPos: Int, toColPos: Int) {
        // Clear if any previous messages displayed in the UI
        CharacterModel.setMessage("")
        CharacterModel.moveCharacter(fromRowPos,fromColPos,toRowPos,toColPos)
        getTurn()
        getMessage()
        findViewById<GameStage>(R.id.stage).invalidate()
    }

    override fun finishMovement() {
        // Clear if any previous messages displayed in the UI
        CharacterModel.setMessage("")
        CharacterModel.finishMovement()
        getMessage()
        getTurn()
    }
    // For updating UI elements
    override fun getTurn(): String? {
        var curTurn = CharacterModel.getTurn()
        findViewById<TextView>(R.id.turn).text = "Turn: $curTurn"
        // UI highlights on each turn
        if(curTurn.contains("GOLD")) {
            findViewById<Button>(R.id.btn_silver_finish).isEnabled = false
            findViewById<Button>(R.id.btn_finish).isEnabled = true
            findViewById<Button>(R.id.btn_silver_finish).setBackgroundColor(Color.LTGRAY)
            findViewById<Button>(R.id.btn_finish).setBackgroundColor(Color.parseColor("#5eba7d"))
        }else{
            findViewById<Button>(R.id.btn_finish).isEnabled = false
            findViewById<Button>(R.id.btn_silver_finish).isEnabled = true
            findViewById<Button>(R.id.btn_silver_finish).setBackgroundColor(Color.parseColor("#2196f3"))
            findViewById<Button>(R.id.btn_finish).setBackgroundColor(Color.LTGRAY)
        }
        return null;
    }

    override fun getMessage(): String? {
        findViewById<TextView>(R.id.message).text = CharacterModel.getMessage()
        return null;
    }
}


