package com.example.lessontictactoe

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lessontictactoe.ui.theme.TicTacToe
import kotlinx.coroutines.delay


@Composable
fun TicTacToeApp() {
    var darkThemeEnabled by remember { mutableStateOf(false) }
    val colors = if (darkThemeEnabled) darkColorScheme() else lightColorScheme()

    Surface(color = colors.background, modifier = Modifier.fillMaxSize()) {
        TicTacToe(darkTheme = darkThemeEnabled) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(onClick = { darkThemeEnabled = !darkThemeEnabled }) {
                        Text(
                            text = if (darkThemeEnabled) "Light" else "Dark"
                        )
                    }
                }
                MainScreen(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    var boardSize by remember { mutableIntStateOf(0) }
    var field by remember { mutableStateOf(listOf<String>()) }
    var currentPlayer by remember { mutableStateOf("X") }
    var gameFinished by remember { mutableStateOf(false) }
    var gameResultMessage by remember { mutableStateOf("") }
    var scoreX by remember { mutableIntStateOf(0) }
    var scoreO by remember { mutableIntStateOf(0) }
    var timeLeft by remember { mutableIntStateOf(10) }

    val timerDuration = 10

    fun checkForWinner(board: List<String>, size: Int, player: String): Boolean {
        for (row in 0 until size) {
            if ((0 until size).all { col -> board[row * size + col] == player }) {
                return true
            }
        }
        for (col in 0 until size) {
            if ((0 until size).all { row -> board[row * size + col] == player }) {
                return true
            }
        }
        if ((0 until size).all { i -> board[i * size + i] == player }) {
            return true
        }
        if ((0 until size).all { i -> board[i * size + (size - 1 - i)] == player }) {
            return true
        }
        return false
    }

    fun checkForDraw(board: List<String>): Boolean {
        return board.all { it != "_" }
    }

    LaunchedEffect(currentPlayer, gameFinished, boardSize) {
        if (boardSize != 0 && !gameFinished) {
            timeLeft = timerDuration
            while (timeLeft > 0 && !gameFinished) {
                delay(1000L)
                timeLeft -= 1
            }
            if (timeLeft == 0 && !gameFinished) {
                currentPlayer = if (currentPlayer == "X") "O" else "X"
                timeLeft = timerDuration
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    )
    {
        Text(
            text = "Tic Tac Toe",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign = TextAlign.Center
        )
        if (boardSize == 0) {
            scoreO = 0
            scoreX = 0

            Text("Choose board size:",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground)

            Row(
                modifier = Modifier.padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                listOf(3, 4, 5).forEach { size ->
                    Button(onClick = {
                        boardSize = size
                        field = List(size * size) { "_" }
                        currentPlayer = "X"
                        gameFinished = false
                        gameResultMessage = ""
                    }) {
                        Text("${size}*$size")
                    }
                }
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Player X: $scoreX",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Text(
                    text = "Player O: $scoreO",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            GameBoard(boardSize, field, onCellClick = { index ->
                if (!gameFinished && field[index] == "_") {
                    val newField = field.toMutableList()
                    newField[index] = currentPlayer
                    field = newField

                    if (checkForWinner(field, boardSize, currentPlayer)) {
                        gameFinished = true
                        gameResultMessage = "Player $currentPlayer wins!"

                        if (currentPlayer == "X") {
                            scoreX += 1
                        } else {
                            scoreO += 1
                        }
                    } else if (checkForDraw(field)) {
                        gameFinished = true
                        gameResultMessage = "It's a draw!"
                    } else {
                        currentPlayer = if (currentPlayer == "X") "O" else "X"
                    }
                    timeLeft = timerDuration
                }
            })
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Player $currentPlayer's turn: $timeLeft s",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(end = 16.dp),
                    color = MaterialTheme.colorScheme.onBackground,
                )
                CircularProgressIndicator(
                    progress = timeLeft / timerDuration.toFloat(),
                    modifier = Modifier.size(40.dp),
                    strokeWidth = 4.dp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (boardSize != 0 && gameFinished) {
            Text(
                text = gameResultMessage,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (boardSize != 0) {
            GameControls(
                gameFinished = gameFinished,
                onResetRound = {
                    field = List(boardSize * boardSize) { "_" }
                    currentPlayer = "X"
                    gameFinished = false
                    gameResultMessage = ""
                    timeLeft = timerDuration
                },
                onNextRound = {
                    field = List(boardSize * boardSize) { "_" }
                    currentPlayer = "X"
                    gameFinished = false
                    gameResultMessage = ""
                    timeLeft = timerDuration
                },
                onNewGame = {
                    boardSize = 0
                    field = listOf()
                    currentPlayer = "X"
                    gameFinished = false
                    gameResultMessage = ""
                    scoreX = 0
                    scoreO = 0
                    timeLeft = timerDuration
                }
            )
        }
    }
}
@Composable
fun GameBoard(dim: Int, field: List<String>, onCellClick: (Int) -> Unit)
{
    Column {
        for (row in 0 until dim) {
            Row {
                for (col in 0 until dim) {
                    val index = row * dim + col
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .padding(4.dp)
                            .border(2.dp, MaterialTheme.colorScheme.primary)
                            .clickable { onCellClick(index) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = field[index],
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GameControls(
    onResetRound: () -> Unit,
    onNewGame: () -> Unit,
    onNextRound: () -> Unit,
    gameFinished: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ){
        if (gameFinished) {
            Button(onClick = onNextRound) {
                Text("Next round")
            }
        } else
        {
            Button(onClick = onResetRound){
                Text("Reset round")
            }
        }
        Spacer(modifier = Modifier.size(16.dp))

        Button(onClick = onNewGame) {
            Text("New game")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview()
{
    TicTacToe {
        TicTacToeApp()
    }
}