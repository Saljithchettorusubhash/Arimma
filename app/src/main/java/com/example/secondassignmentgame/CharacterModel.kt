package com.example.secondassignmentgame

import com.example.secondassignmentgame.*


object CharacterModel {
    private val characters = mutableSetOf<ArimaCharacter>()

    init{
        resetCharacters()
    }

    // Function to get the position of a selected character
    fun position(rowPos:Int,colPos:Int) : ArimaCharacter ?{
        try {
            return characters.first { it -> it.colPos == colPos && it.rowPos == rowPos }
        }catch (err: java.lang.Exception){
            return null
        }
    }

    private fun resetCharacters() {
        characters.removeAll(characters)
        //setting rabbits initial position
        for(i in 0..7){ // rows
            characters.add(ArimaCharacter(i,0,ArimaPlayer.SILVER,PlayerStrength.RABBIT, R.drawable.srabit))
            characters.add(ArimaCharacter(i,7,ArimaPlayer.GOLD,PlayerStrength.RABBIT, R.drawable.rabit))
        }
        for (i in 0..1){
            //setting cats initial position
            characters.add(ArimaCharacter(0+i*7,1,ArimaPlayer.SILVER,PlayerStrength.CAT, R.drawable.scat))
            characters.add(ArimaCharacter(0+i*7,6,ArimaPlayer.GOLD,PlayerStrength.CAT, R.drawable.cat))
            //setting dogs initial position
            characters.add(ArimaCharacter(1+i*6,1,ArimaPlayer.SILVER,PlayerStrength.DOG, R.drawable.sdog))
            characters.add(ArimaCharacter(1+i*6,6,ArimaPlayer.GOLD,PlayerStrength.DOG, R.drawable.dog))
            characters.add(ArimaCharacter(6+i*6,1,ArimaPlayer.SILVER,PlayerStrength.DOG, R.drawable.sdog))
            characters.add(ArimaCharacter(6+i*6,6,ArimaPlayer.GOLD,PlayerStrength.DOG, R.drawable.dog))
            //setting horses initial position
            characters.add(ArimaCharacter(2+i*5,1,ArimaPlayer.SILVER,PlayerStrength.HORSE, R.drawable.shorse))
            characters.add(ArimaCharacter(2+i*5,6,ArimaPlayer.GOLD,PlayerStrength.HORSE, R.drawable.horse))
            characters.add(ArimaCharacter(5+i*5,1,ArimaPlayer.SILVER,PlayerStrength.HORSE, R.drawable.shorse))
            characters.add(ArimaCharacter(5+i*5,6,ArimaPlayer.GOLD,PlayerStrength.HORSE, R.drawable.horse))
        }

        //setting camels initial position
        characters.add(ArimaCharacter(3,1,ArimaPlayer.SILVER,PlayerStrength.CAMEL, R.drawable.scamel))
        characters.add(ArimaCharacter(3,6,ArimaPlayer.GOLD,PlayerStrength.CAMEL, R.drawable.camel))

        //setting elephant initial position
        characters.add(ArimaCharacter(4,1,ArimaPlayer.SILVER,PlayerStrength.ELEPHANT, R.drawable.selephant))
        characters.add(ArimaCharacter(4,6,ArimaPlayer.GOLD,PlayerStrength.ELEPHANT, R.drawable.elephant))

    }
}