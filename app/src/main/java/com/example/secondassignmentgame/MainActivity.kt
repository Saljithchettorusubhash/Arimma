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
        getTurn()
    }
    override fun position(rowPos: Int, colPos: Int): ArimaCharacter? {
        return CharacterModel.position(rowPos, colPos)
    }

    override fun moveCharacter(fromRowPos: Int, fromColPos: Int, toRowPos: Int, toColPos: Int) {
        CharacterModel.moveCharacter(fromRowPos,fromColPos,toRowPos,toColPos)
        getTurn()
        //Redraw the stage
        findViewById<GameStage>(R.id.stage).invalidate()
    }

    override fun finishMovement() {
        CharacterModel.finishMovement()
        getTurn()
    }
    // For updating UI elements
    override fun getTurn(): String? {
        var curTurn = CharacterModel.getTurn()
        return null;
    }
}

