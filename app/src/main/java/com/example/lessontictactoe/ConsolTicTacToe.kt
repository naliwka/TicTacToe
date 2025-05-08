package com.example.lessontictactoe

const val DIM=3

enum class GameState {
    IN_PROGRESS,
    CROSS_WIN,
    NOUGHT_WIN,
    DRAW
}

fun checkGameState (field: List<CellState>) : GameState
{
    val winLines=listOf(
        listOf(0, 1,2),
        listOf(3, 4,5),
        listOf(6, 7,8),
        listOf(0, 3, 6),
        listOf(1, 4, 7),
        listOf(2, 5, 8),
        listOf(0,4,8),
        listOf(2, 4, 6)
    )

    for (line in winLines)
    {
        val(a, b, c)=line
        if(field[a]!= CellState.EMPTY && field[a]==field[b] && field[b]==field[c])
        {
           return when(field[a])
           {
               CellState.CROSS -> GameState.CROSS_WIN
               CellState.NOUGHT -> GameState.NOUGHT_WIN
               else -> GameState.IN_PROGRESS
           }
        }
    }
    return if (field.any { it == CellState.EMPTY }) {
        GameState.IN_PROGRESS
    } else {
        GameState.DRAW
    }
}


enum class Player {
    CROSS,
    NOUGHT
}
//-------------------------------------------------------
val Player.mark: CellState
    get()=when(this)
    {
        Player.CROSS -> CellState.CROSS
        Player.NOUGHT -> CellState.NOUGHT
    }

enum class CellState {
    EMPTY,
    CROSS,
    NOUGHT
}

val field=MutableList(DIM*DIM) {
    CellState.EMPTY
}

fun printField(field: List<CellState>) {
    for (row in 0 until DIM) {
        for(col in 0 until DIM){
            val index=row*DIM+col
            val symbol=when(field[index])
            {
                CellState.EMPTY -> "_"
                CellState.CROSS -> "X"
                CellState.NOUGHT -> "0"
            }
            print("$symbol")
        }
        println()
    }

}


fun main()
{
    val field= MutableList(DIM*DIM) { CellState.EMPTY }
    var currentPlayer= Player.CROSS

    while (true) {
        printField(field)

        println("Player's move ${if(currentPlayer== Player.CROSS) "X" else "0"}")

        println("Enter the move(row and col, separated by space)")
        val input=readln()
         val(row, col)=input.split(" ").map {it.toInt()}

        val index=row*DIM+col
        if (field[index]!= CellState.EMPTY) {
            println("Try again")
            continue
        }

        field[index]= currentPlayer.mark

        val state=checkGameState(field)
        when(state)
        {
            GameState.CROSS_WIN -> {
                printField(field)
                println("Win X")
                break
            }

            GameState.NOUGHT_WIN -> {
                printField(field)
                println("Win 0")
                break
            }
            GameState.DRAW -> {
                printField(field)
                println("Draw")
                break
            }

            GameState.IN_PROGRESS -> { }
        }

        currentPlayer=if(currentPlayer== Player.CROSS) Player.NOUGHT else Player.CROSS

        println()
    }


}