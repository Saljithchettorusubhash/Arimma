package com.example.secondassignmentgame

import android.app.Activity
import android.os.Bundle
import com.example.secondassignmentgame.CharacterModel
import com.example.secondassignmentgame.ArimaCharacter

class MainActivity : Activity(), ICharacterService {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val stage = findViewById<GameStage>(R.id.stage)
        // Connect to consume the service implementation
        // to use its service in the view
        findViewById<GameStage>(R.id.stage).characterService = this

    }
    override fun position(rowPos: Int, colPos: Int): ArimaCharacter? {
        return CharacterModel.position(rowPos, colPos)
    }

}
