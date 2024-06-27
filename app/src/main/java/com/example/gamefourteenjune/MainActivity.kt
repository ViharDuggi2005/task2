package com.example.gamefourteenjune

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.gamefourteenjune.ui.theme.GamefourteenjuneTheme
import kotlinx.coroutines.delay
import kotlin.math.abs
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GamefourteenjuneTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GameApp()
                }
            }
        }
    }
}
@Composable
fun GameApp() {
    var showSplash by remember { mutableStateOf(true) }
    val context = LocalContext.current

    if (showSplash) {
        SplashScreen(onTimeout = { showSplash = false })
    } else {
        MyCanvas()
    }
}

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    val tomAndJerryImage = ImageBitmap.imageResource(R.drawable.tom_and_jerry)

    LaunchedEffect(Unit) {
        delay(3000) // Show the splash screen for 3 seconds
        onTimeout()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Cheese Chase",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Image(
                bitmap = tomAndJerryImage,
                contentDescription = "Tom and Jerry",
                modifier = Modifier.size(200.dp)
            )
        }
    }
}
/*@Composable
fun MyCanvas() {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val context = LocalContext.current
    val jerryImageBitmap = ImageBitmap.imageResource(R.drawable.jerry)
    val tomImageBitmap = ImageBitmap.imageResource(R.drawable.tom)
    val screenWidth = with(density) { configuration.screenWidthDp.dp.toPx() }
    val screenHeight = with(density) { configuration.screenHeightDp.dp.toPx() }
    val pathWidth = screenWidth / 3
    val paths = listOf(pathWidth / 2, screenWidth / 2, screenWidth - pathWidth / 2)
    var circlePosition by remember { mutableStateOf(Offset(screenWidth / 2, screenHeight - 500f)) }
    var gameEnd by remember { mutableStateOf(false) }
    var obstacles by remember { mutableStateOf(generateInitialObstacles(pathWidth, screenHeight)) }
    var score by remember { mutableIntStateOf(10 * (arrayListNumbers.size) + 10) }
    val mediaPlayer = remember { MediaPlayer.create(context, R.raw.jerryjump) }
    val mediaPlayer2 = remember { MediaPlayer.create(context, R.raw.bgsound) }
    var collisionCount by remember { mutableIntStateOf(0) }

    LaunchedEffect(gameEnd) {
        if (!gameEnd) {
            mediaPlayer2.start()
            while (!gameEnd) {
                delay(50)
                circlePosition = circlePosition.copy(y = circlePosition.y - 5f) // Move circle upwards
                obstacles = obstacles.map { it.copy(y = it.y + 15) }.filter { it.y < screenHeight + 40 }
                var spawnRate = 0.02
                var points = 10
                if (score in 100..249) {
                    spawnRate = 0.025
                    points = 15
                } else if (score >= 250) {
                    spawnRate = 0.03
                    points = 20
                }
                if (Random.nextFloat() < spawnRate) {
                    obstacles = obstacles + generateRandomObstacle(pathWidth, screenWidth)
                    score += points
                }

                if (circlePosition.y < 200f) {
                    circlePosition = Offset(screenWidth / 2, screenHeight - 900f) // Reset position
                }

                var collided = false
                for (square in obstacles) {
                    if (isColliding(circlePosition, square, 50f, 70f)) { // Adjusted collision detection size
                        collided = true
                        break
                    }
                }

                if (collided) {
                    collisionCount++
                    if (collisionCount >= 2) {
                        gameEnd = true
                        mediaPlayer2.pause() // Pause background sound on game end
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    if (offset.y < circlePosition.y) {
                        val nearestPathX = paths.minByOrNull { abs(it - offset.x) } ?: (screenWidth / 2)
                        circlePosition = circlePosition.copy(x = nearestPathX)
                        mediaPlayer.start()
                    }
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Draw paths
            drawRect(color = Color(0xFFE5E5E5))
            val canvasWidth = size.width
            val canvasHeight = size.height
            val spacing = 37.dp.toPx()
            val rectWidth = 100.dp.toPx()
            val top = 0f
            for (i in 0 until 3) {
                val left = spacing + i * (rectWidth + spacing)
                drawRect(
                    color = Color(0xFF61C0BF),
                    topLeft = Offset(left, top),
                    size = Size(rectWidth, canvasHeight)
                )
            }

            for (square in obstacles) {
                drawRect(
                    color = Color.Blue,
                    topLeft = Offset(square.x, square.y),
                    size = Size(70f, 70f)
                )
            }

            if (gameEnd) {
                drawImage(
                    image = tomImageBitmap,
                    topLeft = Offset(
                        circlePosition.x - tomImageBitmap.width / 2,
                        circlePosition.y - tomImageBitmap.height / 2
                    ),
                    alpha = 1f
                )
            } else {
                val imageLeft1 = circlePosition.x - (tomImageBitmap.width / 2)
                val imageTop1 = screenHeight - tomImageBitmap.height

                drawImage(
                    image = tomImageBitmap,
                    topLeft = Offset(imageLeft1, imageTop1),
                    alpha = 1f
                )
            }

            val imageLeft = circlePosition.x - 370f / 2
            val imageTop = circlePosition.y - 200f / 2
            drawImage(
                image = jerryImageBitmap,
                topLeft = Offset(imageLeft, imageTop),
                alpha = 1f
            )
        }

        Text(
            text = "Score: $score",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        )

        if (gameEnd) {
            AlertDialog(
                onDismissRequest = {},
                title = { Text(text = "Game End") },
                text = { Text("Score is $score") },
                confirmButton = {
                    Button(onClick = {
                        gameEnd = false
                        circlePosition = Offset(screenWidth / 2, screenHeight - 100f)
                        obstacles = generateInitialObstacles(pathWidth, screenHeight)
                        score = 0
                        prevIndex = -1
                        arrayListNumbers.clear()
                        collisionCount = 0 // Reset collision count
                    }, modifier = Modifier
                        .width(250.dp)
                        .height(40.dp)) {
                        Text("Play Again")
                    }
                },
            )
        }
    }
}

data class Square(val x: Float, val y: Float)
var arrayListNumbers = ArrayList<Int>()
fun generateInitialObstacles(pathWidth: Float, screenHeight: Float): List<Square> {
    return List(Random.nextInt(3)) { i ->
        val pathIndex = Random.nextInt(3)
        arrayListNumbers.add(pathIndex)
        Square(
            x = pathIndex * pathWidth + pathWidth / 2 - 20,
            y = i / 3 * 200f
        )
    }
}

var prevIndex = -1
fun generateRandomObstacle(pathWidth: Float, screenWidth: Float): Square {
    val arrayListNumbers = arrayListOf(0, 1, 2)
    var pathIndex: Int
    if (prevIndex == -1) {
        do {
            pathIndex = Random.nextInt(3)
        } while (pathIndex !in arrayListNumbers)
    } else {
        do {
            pathIndex = Random.nextInt(3)
        } while (pathIndex == prevIndex)
    }
    prevIndex = pathIndex
    return Square(
        x = pathIndex * pathWidth + pathWidth / 2 - 20,
        y = -40f
    )
}

fun isColliding(circlePos: Offset, squarePos: Square, circleRadius: Float, squareSize: Float): Boolean {
    val closestX = circlePos.x.coerceIn(squarePos.x, squarePos.x + squareSize)
    val closestY = circlePos.y.coerceIn(squarePos.y, squarePos.y + squareSize)
    val distanceX = circlePos.x - closestX
    val distanceY = circlePos.y - closestY
    return (distanceX * distanceX + distanceY * distanceY) < (circleRadius * circleRadius)
}
*/

/*@Composable
fun MyCanvas() {
    var collisionDetected = false
    var collisionDetected2 = false
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val context = LocalContext.current
    val jerryImageBitmap = ImageBitmap.imageResource(R.drawable.jerry)
    val tomImageBitmap = ImageBitmap.imageResource(R.drawable.tom)
    val screenWidth = with(density) { configuration.screenWidthDp.dp.toPx() }
    val screenHeight = with(density) { configuration.screenHeightDp.dp.toPx() }
    val pathWidth = screenWidth / 3
    val paths = listOf(pathWidth / 2, screenWidth / 2, screenWidth - pathWidth / 2)
    var circlePosition by remember {
        mutableStateOf(Offset(screenWidth / 2, screenHeight - 500f))
    }
    var k = 0
    var gameEnd by remember { mutableStateOf(false) }
    var blackCirclePosition by remember { mutableStateOf(Offset(0f, 0f)) }
    var obstacles by remember { mutableStateOf(generateInitialObstacles(pathWidth, screenHeight)) }
    var score by remember { mutableIntStateOf(10 * (arrayListNumbers.size)+10) }
    var mediaPlayer = MediaPlayer.create(context, R.raw.jerryjump)
    var mediaPlayer2 = MediaPlayer.create(context, R.raw.bgsound)
    var collisionCount by remember { mutableStateOf(0) }

    LaunchedEffect(gameEnd) {
        if (!gameEnd) {
            while (!gameEnd) {
                mediaPlayer2.start()
                delay(50)
                circlePosition = circlePosition.copy(y = circlePosition.y - 5f) // Move circle upwards
                obstacles = obstacles.map { it.copy(y = it.y + 15) }.filter { it.y < screenHeight + 40 }
                var g = 0.02
                var h = 10
                if (score in 100..249) {
                    g = 0.025
                    h = 15
                } else if (score >= 250) {
                    g = 0.03
                    h = 20
                }
                if (Random.nextFloat() < g) {
                    obstacles = obstacles + generateRandomObstacle(pathWidth, screenWidth)
                    score += h
                }

                if (circlePosition.y < 200f) {
                    circlePosition = Offset(screenWidth / 2, screenHeight - 900f) // Reset position
                }

                obstacles.forEach { square ->
                    if (isColliding(circlePosition, square, 50f, 40f)) {
                        if (k<=1) {
                            k++
                            gameEnd=false
                        }
                        else {
                            gameEnd=true
                        }

                }
                }

                /*obstacles.forEach { square ->
                    if (isColliding(circlePosition, square, 50f, 40f)&&k==0) {
                        k++
                    }

                }
                if (k==1)
                {
                obstacles.forEach { square ->
                    if (isColliding(circlePosition, square, 50f, 40f)) {
                        gameEnd=true
                    }

                }*/
            }

        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    if (offset.y < circlePosition.y) {
                        val nearestPathX =
                            paths.minByOrNull { abs(it - offset.x) } ?: (screenWidth / 2)
                        circlePosition = circlePosition.copy(x = nearestPathX)
                        mediaPlayer.start()
                    }
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Draw paths
            drawRect(color = Color(4294309340))
            val canvasWidth = size.width
            val canvasHeight = size.height
            val spacing = 37.dp.toPx()
            val rectWidth = 100.dp.toPx()
            val top = 0f
            for (i in 0 until 3) {
                val left = spacing + i * (rectWidth + spacing)
                drawRect(
                    color = Color(4287090411),
                    topLeft = Offset(left, top),
                    size = Size(rectWidth, canvasHeight)
                )
            }

            obstacles.forEach { square ->
                drawRect(
                    color = Color.Blue,
                    topLeft = Offset(square.x, square.y),
                    size = Size(70f, 70f)
                )
            }
            if (gameEnd) {
                drawImage(
                    image = tomImageBitmap,
                    topLeft = Offset(
                        blackCirclePosition.x - tomImageBitmap.width / 2,
                        blackCirclePosition.y - tomImageBitmap.height / 2
                    ),
                    alpha = 1f
                )
            } else {
                // Draw image at the bottom center of the screen when the game is not ended
                val imageLeft1 = circlePosition.x - (tomImageBitmap.width / 2)
                val imageTop1 = screenHeight - tomImageBitmap.height // Position it at the bottom

                drawImage(
                    image = tomImageBitmap,
                    topLeft = Offset(imageLeft1, imageTop1),
                    alpha = 1f
                )
            }
            // Calculate the new topLeft position based on the desired width and height
            val imageLeft = circlePosition.x - 370f / 2
            val imageTop = circlePosition.y - 200f / 2
            drawImage(
                image = jerryImageBitmap,
                topLeft = Offset(imageLeft, imageTop),
                alpha = 1f
            )
        }

        Text(
            text = "Score: $score",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        )

        if (gameEnd && k==2) {
            AlertDialog(
                onDismissRequest = {},
                title = { Text(text = "Game End") },
                text = { Text("Score is $score") },
                confirmButton = {
                    Button(onClick = {
                        gameEnd = false
                        circlePosition = Offset(screenWidth / 2, screenHeight - 100f)
                        obstacles = generateInitialObstacles(pathWidth, screenHeight)
                        score = 0
                        prevIndex = -1
                        arrayListNumbers = ArrayList<Int>()
                    }, modifier = Modifier
                        .width(250.dp)  // Adjust width as needed
                        .height(40.dp)) {
                        Text("Play Again")
                    }
                },
            )
        }
    }
}

data class Square(val x: Float, val y: Float)
var arrayListNumbers = ArrayList<Int>()
fun generateInitialObstacles(pathWidth: Float, screenHeight: Float): List<Square> {
    return List(Random.nextInt(3)) { i ->
        val pathIndex = Random.nextInt(3)
        arrayListNumbers.add(pathIndex)
        Square(
            x = pathIndex * pathWidth + pathWidth / 2 - 20,
            y = i / 3 * 200f
        )
    }
}

var prevIndex = -1
fun generateRandomObstacle(pathWidth: Float, screenWidth: Float): Square {
    val arrayListNumbers = arrayListOf(0, 1, 2)
    val klm = arrayListOf(0, 0) // List of valid path indices
    var pathIndex: Int
    if (prevIndex == -1) {
        // Initial random selection
        do {
            pathIndex = Random.nextInt(3) // Generate random index from 0 to 2
        } while (pathIndex !in arrayListNumbers)
    } else {
        // Subsequent random selection, ensure it's different from prevIndex
        do {
            pathIndex = Random.nextInt(3)
        } while (pathIndex == prevIndex)
    }
    prevIndex = pathIndex // Update prevIndex to current pathIndex
    // Return a Square object with calculated x position and initial y position
    return Square(
        x = pathIndex * pathWidth + pathWidth / 2 - 20,
        y = -40f // Initial y position, adjust as needed
    )
}

fun isColliding(circlePos: Offset, squarePos: Square, circleRadius: Float, squareSize: Float): Boolean {
    val closestX = circlePos.x.coerceIn(squarePos.x, squarePos.x + squareSize)
    val closestY = circlePos.y.coerceIn(squarePos.y, squarePos.y + squareSize)
    val distanceX = circlePos.x - closestX
    val distanceY = circlePos.y - closestY
    return (distanceX * distanceX + distanceY * distanceY) < (circleRadius * circleRadius)
}*/
/*@Composable
fun MyCanvas() {
    var collisionDetected = false
    var collisionDetected2 = false
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val context = LocalContext.current
    val jerryImageBitmap = ImageBitmap.imageResource(R.drawable.jerry)
    val tomImageBitmap = ImageBitmap.imageResource(R.drawable.tom)
    val screenWidth = with(density) { configuration.screenWidthDp.dp.toPx() }
    val screenHeight = with(density) { configuration.screenHeightDp.dp.toPx() }
    val pathWidth = screenWidth / 3
    val paths = listOf(pathWidth / 2, screenWidth / 2, screenWidth - pathWidth / 2)
    var circlePosition by remember {
        mutableStateOf(Offset(screenWidth / 2, screenHeight - 500f))
    }
    var k = 0
    var gameEnd by remember { mutableStateOf(false) }
    var blackCirclePosition by remember { mutableStateOf(Offset(0f, 0f)) }
    var obstacles by remember { mutableStateOf(generateInitialObstacles(pathWidth, screenHeight)) }
    var score by remember { mutableIntStateOf(10 * (arrayListNumbers.size)+10) }
    var mediaPlayer = MediaPlayer.create(context, R.raw.jerryjump)
    var mediaPlayer2 = MediaPlayer.create(context, R.raw.bgsound)
    var collisionCount by remember { mutableStateOf(0) }

    LaunchedEffect(gameEnd) {
        if (!gameEnd) {
            while (!gameEnd) {
                mediaPlayer2.start()
                delay(50)
                circlePosition = circlePosition.copy(y = circlePosition.y - 5f) // Move circle upwards
                obstacles = obstacles.map { it.copy(y = it.y + 15) }.filter { it.y < screenHeight + 40 }
                var g = 0.02
                var h = 10
                if (score in 100..249) {
                    g = 0.025
                    h = 15
                } else if (score >= 250) {
                    g = 0.03
                    h = 20
                }
                if (Random.nextFloat() < g) {
                    obstacles = obstacles + generateRandomObstacle(pathWidth, screenWidth)
                    score += h
                }

                if (circlePosition.y < 200f) {
                    circlePosition = Offset(screenWidth / 2, screenHeight - 900f) // Reset position
                }

                obstacles.forEach { square ->
                    if (isColliding(circlePosition, square, 50f, 40f)) {
                        collisionCount++
                        if (collisionCount >= 2) {
                            gameEnd = true
                        }
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    if (offset.y < circlePosition.y) {
                        val nearestPathX =
                            paths.minByOrNull { abs(it - offset.x) } ?: (screenWidth / 2)
                        circlePosition = circlePosition.copy(x = nearestPathX)
                        mediaPlayer.start()
                    }
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Draw paths
            drawRect(color = Color(4294309340))
            val canvasWidth = size.width
            val canvasHeight = size.height
            val spacing = 37.dp.toPx()
            val rectWidth = 100.dp.toPx()
            val top = 0f
            for (i in 0 until 3) {
                val left = spacing + i * (rectWidth + spacing)
                drawRect(
                    color = Color(4287090411),
                    topLeft = Offset(left, top),
                    size = Size(rectWidth, canvasHeight)
                )
            }

            obstacles.forEach { square ->
                drawRect(
                    color = Color.Blue,
                    topLeft = Offset(square.x, square.y),
                    size = Size(70f, 70f)
                )
            }
            if (gameEnd) {
                drawImage(
                    image = tomImageBitmap,
                    topLeft = Offset(
                        blackCirclePosition.x - tomImageBitmap.width / 2,
                        blackCirclePosition.y - tomImageBitmap.height / 2
                    ),
                    alpha = 1f
                )
            } else {
                // Draw image at the bottom center of the screen when the game is not ended
                val imageLeft1 = circlePosition.x - (tomImageBitmap.width / 2)
                val imageTop1 = screenHeight - tomImageBitmap.height // Position it at the bottom

                drawImage(
                    image = tomImageBitmap,
                    topLeft = Offset(imageLeft1, imageTop1),
                    alpha = 1f
                )
            }
            // Calculate the new topLeft position based on the desired width and height
            val imageLeft = circlePosition.x - 370f / 2
            val imageTop = circlePosition.y - 200f / 2
            drawImage(
                image = jerryImageBitmap,
                topLeft = Offset(imageLeft, imageTop),
                alpha = 1f
            )
        }

        Text(
            text = "Score: $score",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        )

        if (gameEnd) {
            AlertDialog(
                onDismissRequest = {},
                title = { Text(text = "Game End") },
                text = { Text("Score is $score") },
                confirmButton = {
                    Button(onClick = {
                        gameEnd = false
                        circlePosition = Offset(screenWidth / 2, screenHeight - 100f)
                        obstacles = generateInitialObstacles(pathWidth, screenHeight)
                        score = 0
                        prevIndex = -1
                        arrayListNumbers = ArrayList<Int>()
                        collisionCount = 0 // Reset collision count
                    }, modifier = Modifier
                        .width(250.dp)  // Adjust width as needed
                        .height(40.dp)) {
                        Text("Play Again")
                    }
                },
            )
        }
    }
}
data class Square(val x: Float, val y: Float)
var arrayListNumbers = ArrayList<Int>()
fun generateInitialObstacles(pathWidth: Float, screenHeight: Float): List<Square> {
    return List(Random.nextInt(3)) { i ->
        val pathIndex = Random.nextInt(3)
        arrayListNumbers.add(pathIndex)
        Square(
            x = pathIndex * pathWidth + pathWidth / 2 - 20,
            y = i / 3 * 200f
        )
    }
}

var prevIndex = -1
fun generateRandomObstacle(pathWidth: Float, screenWidth: Float): Square {
    val arrayListNumbers = arrayListOf(0, 1, 2)
    val klm = arrayListOf(0, 0) // List of valid path indices
    var pathIndex: Int
    if (prevIndex == -1) {
        // Initial random selection
        do {
            pathIndex = Random.nextInt(3) // Generate random index from 0 to 2
        } while (pathIndex !in arrayListNumbers)
    } else {
        // Subsequent random selection, ensure it's different from prevIndex
        do {
            pathIndex = Random.nextInt(3)
        } while (pathIndex == prevIndex)
    }
    prevIndex = pathIndex // Update prevIndex to current pathIndex
    // Return a Square object with calculated x position and initial y position
    return Square(
        x = pathIndex * pathWidth + pathWidth / 2 - 20,
        y = -40f // Initial y position, adjust as needed
    )
}

fun isColliding(circlePos: Offset, squarePos: Square, circleRadius: Float, squareSize: Float): Boolean {
    val closestX = circlePos.x.coerceIn(squarePos.x, squarePos.x + squareSize)
    val closestY = circlePos.y.coerceIn(squarePos.y, squarePos.y + squareSize)
    val distanceX = circlePos.x - closestX
    val distanceY = circlePos.y - closestY
    return (distanceX * distanceX + distanceY * distanceY) < (circleRadius * circleRadius)
}*/
/*@Composable
fun MyCanvas() {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val context = LocalContext.current
    val jerryImageBitmap = ImageBitmap.imageResource(R.drawable.jerry)
    val tomImageBitmap = ImageBitmap.imageResource(R.drawable.tom)
    val screenWidth = with(density) { configuration.screenWidthDp.dp.toPx() }
    val screenHeight = with(density) { configuration.screenHeightDp.dp.toPx() }
    val pathWidth = screenWidth / 3
    val paths = listOf(pathWidth / 2, screenWidth / 2, screenWidth - pathWidth / 2)
    var circlePosition by remember {
        mutableStateOf(Offset(screenWidth / 2, screenHeight - 500f))
    }
    var gameEnd by remember { mutableStateOf(false) }
    var obstacles by remember { mutableStateOf(generateInitialObstacles(pathWidth, screenHeight)) }
    var score by remember { mutableIntStateOf(10 * (arrayListNumbers.size)+10) }
    val mediaPlayer = remember { MediaPlayer.create(context, R.raw.jerryjump) }
    val mediaPlayer2 = remember { MediaPlayer.create(context, R.raw.bgsound) }
    var collisionCount by remember { mutableStateOf(0) }

    LaunchedEffect(gameEnd) {
        if (!gameEnd) {
            mediaPlayer2.start()
            while (!gameEnd) {
                delay(50)
                circlePosition = circlePosition.copy(y = circlePosition.y - 5f) // Move circle upwards
                obstacles = obstacles.map { it.copy(y = it.y + 15) }.filter { it.y < screenHeight + 40 }
                var spawnRate = 0.02
                var points = 10
                if (score in 100..249) {
                    spawnRate = 0.025
                    points = 15
                } else if (score >= 250) {
                    spawnRate = 0.03
                    points = 20
                }
                if (Random.nextFloat() < spawnRate) {
                    obstacles = obstacles + generateRandomObstacle(pathWidth, screenWidth)
                    score += points
                }

                if (circlePosition.y < 200f) {
                    circlePosition = Offset(screenWidth / 2, screenHeight - 900f) // Reset position
                }

                obstacles.forEach { square ->
                    if (isColliding(circlePosition, square, 50f, 40f)) {
                        collisionCount++
                        if (collisionCount >= 2) {
                            gameEnd = true
                        }
                    }
                }
            }
            mediaPlayer2.pause()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    if (offset.y < circlePosition.y) {
                        val nearestPathX =
                            paths.minByOrNull { abs(it - offset.x) } ?: (screenWidth / 2)
                        circlePosition = circlePosition.copy(x = nearestPathX)
                        mediaPlayer.start()
                    }
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Draw paths
            drawRect(color = Color(4294309340))
            val canvasWidth = size.width
            val canvasHeight = size.height
            val spacing = 37.dp.toPx()
            val rectWidth = 100.dp.toPx()
            val top = 0f
            for (i in 0 until 3) {
                val left = spacing + i * (rectWidth + spacing)
                drawRect(
                    color = Color(4287090411),
                    topLeft = Offset(left, top),
                    size = Size(rectWidth, canvasHeight)
                )
            }

            obstacles.forEach { square ->
                drawRect(
                    color = Color.Blue,
                    topLeft = Offset(square.x, square.y),
                    size = Size(70f, 70f)
                )
            }
            if (gameEnd) {
                drawImage(
                    image = tomImageBitmap,
                    topLeft = Offset(
                        circlePosition.x - tomImageBitmap.width / 2,
                        circlePosition.y - tomImageBitmap.height / 2
                    ),
                    alpha = 1f
                )
            } else {
                val imageLeft1 = circlePosition.x - (tomImageBitmap.width / 2)
                val imageTop1 = screenHeight - tomImageBitmap.height

                drawImage(
                    image = tomImageBitmap,
                    topLeft = Offset(imageLeft1, imageTop1),
                    alpha = 1f
                )
            }
            val imageLeft = circlePosition.x - 370f / 2
            val imageTop = circlePosition.y - 200f / 2
            drawImage(
                image = jerryImageBitmap,
                topLeft = Offset(imageLeft, imageTop),
                alpha = 1f
            )
        }

        Text(
            text = "Score: $score",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        )

        if (gameEnd) {
            AlertDialog(
                onDismissRequest = {},
                title = { Text(text = "Game End") },
                text = { Text("Score is $score") },
                confirmButton = {
                    Button(onClick = {
                        gameEnd = false
                        circlePosition = Offset(screenWidth / 2, screenHeight - 100f)
                        obstacles = generateInitialObstacles(pathWidth, screenHeight)
                        score = 0
                        prevIndex = -1
                        arrayListNumbers = ArrayList<Int>()
                        collisionCount = 0 // Reset collision count
                    }, modifier = Modifier
                        .width(250.dp)  // Adjust width as needed
                        .height(40.dp)) {
                        Text("Play Again")
                    }
                },
            )
        }
    }
}

data class Square(val x: Float, val y: Float)
var arrayListNumbers = ArrayList<Int>()
fun generateInitialObstacles(pathWidth: Float, screenHeight: Float): List<Square> {
    return List(Random.nextInt(3)) { i ->
        val pathIndex = Random.nextInt(3)
        arrayListNumbers.add(pathIndex)
        Square(
            x = pathIndex * pathWidth + pathWidth / 2 - 20,
            y = i / 3 * 200f
        )
    }
}

var prevIndex = -1
fun generateRandomObstacle(pathWidth: Float, screenWidth: Float): Square {
    val arrayListNumbers = arrayListOf(0, 1, 2)
    val klm = arrayListOf(0, 0) // List of valid path indices
    var pathIndex: Int
    if (prevIndex == -1) {
        do {
            pathIndex = Random.nextInt(3)
        } while (pathIndex !in arrayListNumbers)
    } else {
        do {
            pathIndex = Random.nextInt(3)
        } while (pathIndex == prevIndex)
    }
    prevIndex = pathIndex
    return Square(
        x = pathIndex * pathWidth + pathWidth / 2 - 20,
        y = -40f
    )
}

fun isColliding(circlePos: Offset, squarePos: Square, circleRadius: Float, squareSize: Float): Boolean {
    val closestX = circlePos.x.coerceIn(squarePos.x, squarePos.x + squareSize)
    val closestY = circlePos.y.coerceIn(squarePos.y, squarePos.y + squareSize)
    val distanceX = circlePos.x - closestX
    val distanceY = circlePos.y - closestY
    return (distanceX * distanceX + distanceY * distanceY) < (circleRadius * circleRadius)
}*/
/*@Composable
fun MyCanvas() {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val context = LocalContext.current
    val jerryImageBitmap = ImageBitmap.imageResource(R.drawable.jerry)
    val tomImageBitmap = ImageBitmap.imageResource(R.drawable.tom)
    val screenWidth = with(density) { configuration.screenWidthDp.dp.toPx() }
    val screenHeight = with(density) { configuration.screenHeightDp.dp.toPx() }
    val pathWidth = screenWidth / 3
    val paths = listOf(pathWidth / 2, screenWidth / 2, screenWidth - pathWidth / 2)
    var circlePosition by remember {
        mutableStateOf(Offset(screenWidth / 2, screenHeight - 500f))
    }
    var gameEnd by remember { mutableStateOf(false) }
    var obstacles by remember { mutableStateOf(generateInitialObstacles(pathWidth, screenHeight)) }
    var score by remember { mutableIntStateOf(10 * (arrayListNumbers.size)+10) }
    val mediaPlayer = remember { MediaPlayer.create(context, R.raw.jerryjump) }
    val mediaPlayer2 = remember { MediaPlayer.create(context, R.raw.bgsound) }
    var collisionCount by remember { mutableStateOf(0) }

    LaunchedEffect(gameEnd) {
        if (!gameEnd) {
            mediaPlayer2.start()
            while (!gameEnd) {
                delay(50)
                circlePosition = circlePosition.copy(y = circlePosition.y - 5f) // Move circle upwards
                obstacles = obstacles.map { it.copy(y = it.y + 15) }.filter { it.y < screenHeight + 40 }
                var spawnRate = 0.02
                var points = 10
                if (score in 100..249) {
                    spawnRate = 0.025
                    points = 15
                } else if (score >= 250) {
                    spawnRate = 0.03
                    points = 20
                }
                if (Random.nextFloat() < spawnRate) {
                    obstacles = obstacles + generateRandomObstacle(pathWidth, screenWidth)
                    score += points
                }

                if (circlePosition.y < 200f) {
                    circlePosition = Offset(screenWidth / 2, screenHeight - 900f) // Reset position
                }

                for (square in obstacles) {
                    if (isColliding(circlePosition, square, 50f, 40f)) {
                        collisionCount++
                        if (collisionCount >= 2) {
                            gameEnd = true
                            break
                        }
                    }
                }
            }
            mediaPlayer2.pause()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    if (offset.y < circlePosition.y) {
                        val nearestPathX =
                            paths.minByOrNull { abs(it - offset.x) } ?: (screenWidth / 2)
                        circlePosition = circlePosition.copy(x = nearestPathX)
                        mediaPlayer.start()
                    }
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Draw paths
            drawRect(color = Color(4294309340))
            val canvasWidth = size.width
            val canvasHeight = size.height
            val spacing = 37.dp.toPx()
            val rectWidth = 100.dp.toPx()
            val top = 0f
            for (i in 0 until 3) {
                val left = spacing + i * (rectWidth + spacing)
                drawRect(
                    color = Color(4287090411),
                    topLeft = Offset(left, top),
                    size = Size(rectWidth, canvasHeight)
                )
            }

            for (square in obstacles) {
                drawRect(
                    color = Color.Blue,
                    topLeft = Offset(square.x, square.y),
                    size = Size(70f, 70f)
                )
            }
            if (gameEnd) {
                drawImage(
                    image = tomImageBitmap,
                    topLeft = Offset(
                        circlePosition.x - tomImageBitmap.width / 2,
                        circlePosition.y - tomImageBitmap.height / 2
                    ),
                    alpha = 1f
                )
            } else {
                val imageLeft1 = circlePosition.x - (tomImageBitmap.width / 2)
                val imageTop1 = screenHeight - tomImageBitmap.height

                drawImage(
                    image = tomImageBitmap,
                    topLeft = Offset(imageLeft1, imageTop1),
                    alpha = 1f
                )
            }
            val imageLeft = circlePosition.x - 370f / 2
            val imageTop = circlePosition.y - 200f / 2
            drawImage(
                image = jerryImageBitmap,
                topLeft = Offset(imageLeft, imageTop),
                alpha = 1f
            )
        }

        Text(
            text = "Score: $score",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        )

        if (gameEnd && collisionCount>=2) {
            AlertDialog(
                onDismissRequest = {},
                title = { Text(text = "Game End") },
                text = { Text("Score is $score") },
                confirmButton = {
                    Button(onClick = {
                        gameEnd = false
                        circlePosition = Offset(screenWidth / 2, screenHeight - 100f)
                        obstacles = generateInitialObstacles(pathWidth, screenHeight)
                        score = 0
                        prevIndex = -1
                        arrayListNumbers = ArrayList<Int>()
                        collisionCount = 0 // Reset collision count
                    }, modifier = Modifier
                        .width(250.dp)  // Adjust width as needed
                        .height(40.dp)) {
                        Text("Play Again")
                    }
                },
            )
        }
    }
}

data class Square(val x: Float, val y: Float)
var arrayListNumbers = ArrayList<Int>()
fun generateInitialObstacles(pathWidth: Float, screenHeight: Float): List<Square> {
    return List(Random.nextInt(3)) { i ->
        val pathIndex = Random.nextInt(3)
        arrayListNumbers.add(pathIndex)
        Square(
            x = pathIndex * pathWidth + pathWidth / 2 - 20,
            y = i / 3 * 200f
        )
    }
}

var prevIndex = -1
fun generateRandomObstacle(pathWidth: Float, screenWidth: Float): Square {
    val arrayListNumbers = arrayListOf(0, 1, 2)
    val klm = arrayListOf(0, 0) // List of valid path indices
    var pathIndex: Int
    if (prevIndex == -1) {
        do {
            pathIndex = Random.nextInt(3)
        } while (pathIndex !in arrayListNumbers)
    } else {
        do {
            pathIndex = Random.nextInt(3)
        } while (pathIndex == prevIndex)
    }
    prevIndex = pathIndex
    return Square(
        x = pathIndex * pathWidth + pathWidth / 2 - 20,
        y = -40f
    )
}

fun isColliding(circlePos: Offset, squarePos: Square, circleRadius: Float, squareSize: Float): Boolean {
    val closestX = circlePos.x.coerceIn(squarePos.x, squarePos.x + squareSize)
    val closestY = circlePos.y.coerceIn(squarePos.y, squarePos.y + squareSize)
    val distanceX = circlePos.x - closestX
    val distanceY = circlePos.y - closestY
    return (distanceX * distanceX + distanceY * distanceY) < (circleRadius * circleRadius)
}*/
/*@Composable
fun MyCanvas() {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val context = LocalContext.current
    val jerryImageBitmap = ImageBitmap.imageResource(R.drawable.jerry)
    val tomImageBitmap = ImageBitmap.imageResource(R.drawable.tom)
    val screenWidth = with(density) { configuration.screenWidthDp.dp.toPx() }
    val screenHeight = with(density) { configuration.screenHeightDp.dp.toPx() }
    val pathWidth = screenWidth / 3
    val paths = listOf(pathWidth / 2, screenWidth / 2, screenWidth - pathWidth / 2)
    var circlePosition by remember {
        mutableStateOf(Offset(screenWidth / 2, screenHeight - 500f))
    }
    var gameEnd by remember { mutableStateOf(false) }
    var obstacles by remember { mutableStateOf(generateInitialObstacles(pathWidth, screenHeight)) }
    var score by remember { mutableIntStateOf(10 * (arrayListNumbers.size) + 10) }
    val mediaPlayer = remember { MediaPlayer.create(context, R.raw.jerryjump) }
    val mediaPlayer2 = remember { MediaPlayer.create(context, R.raw.bgsound) }
    var collisionCount by remember { mutableStateOf(0) }

    LaunchedEffect(gameEnd) {
        if (!gameEnd) {
            mediaPlayer2.start()
            while (!gameEnd) {
                delay(50)
                circlePosition = circlePosition.copy(y = circlePosition.y - 5f) // Move circle upwards
                obstacles = obstacles.map { it.copy(y = it.y + 15) }.filter { it.y < screenHeight + 40 }
                var spawnRate = 0.02
                var points = 10
                if (score in 100..249) {
                    spawnRate = 0.025
                    points = 15
                } else if (score >= 250) {
                    spawnRate = 0.03
                    points = 20
                }
                if (Random.nextFloat() < spawnRate) {
                    obstacles = obstacles + generateRandomObstacle(pathWidth, screenWidth)
                    score += points
                }

                if (circlePosition.y < 200f) {
                    circlePosition = Offset(screenWidth / 2, screenHeight - 900f) // Reset position
                }

                for (square in obstacles) {
                    if (isColliding(circlePosition, square, 50f, 40f)) {
                        collisionCount++
                        if (collisionCount >= 2) {
                            gameEnd = true
                            break
                        }
                    }
                }
            }
            mediaPlayer2.pause()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    if (offset.y < circlePosition.y) {
                        val nearestPathX =
                            paths.minByOrNull { abs(it - offset.x) } ?: (screenWidth / 2)
                        circlePosition = circlePosition.copy(x = nearestPathX)
                        mediaPlayer.start()
                    }
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Draw paths
            drawRect(color = Color(0xFFE5E5E5))
            val canvasWidth = size.width
            val canvasHeight = size.height
            val spacing = 37.dp.toPx()
            val rectWidth = 100.dp.toPx()
            val top = 0f
            for (i in 0 until 3) {
                val left = spacing + i * (rectWidth + spacing)
                drawRect(
                    color = Color(0xFF61C0BF),
                    topLeft = Offset(left, top),
                    size = Size(rectWidth, canvasHeight)
                )
            }

            for (square in obstacles) {
                drawRect(
                    color = Color.Blue,
                    topLeft = Offset(square.x, square.y),
                    size = Size(70f, 70f)
                )
            }

            if (gameEnd) {
                drawImage(
                    image = tomImageBitmap,
                    topLeft = Offset(
                        circlePosition.x - tomImageBitmap.width / 2,
                        circlePosition.y - tomImageBitmap.height / 2
                    ),
                    alpha = 1f
                )
            } else {
                val imageLeft1 = circlePosition.x - (tomImageBitmap.width / 2)
                val imageTop1 = screenHeight - tomImageBitmap.height

                drawImage(
                    image = tomImageBitmap,
                    topLeft = Offset(imageLeft1, imageTop1),
                    alpha = 1f
                )
            }

            val imageLeft = circlePosition.x - 370f / 2
            val imageTop = circlePosition.y - 200f / 2
            drawImage(
                image = jerryImageBitmap,
                topLeft = Offset(imageLeft, imageTop),
                alpha = 1f
            )
        }

        Text(
            text = "Score: $score",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        )

        if (gameEnd) {
            AlertDialog(
                onDismissRequest = {},
                title = { Text(text = "Game End") },
                text = { Text("Score is $score") },
                confirmButton = {
                    Button(onClick = {
                        gameEnd = false
                        circlePosition = Offset(screenWidth / 2, screenHeight - 100f)
                        obstacles = generateInitialObstacles(pathWidth, screenHeight)
                        score = 0
                        prevIndex = -1
                        arrayListNumbers = ArrayList<Int>()
                        collisionCount = 0 // Reset collision count
                    }, modifier = Modifier
                        .width(250.dp)  // Adjust width as needed
                        .height(40.dp)) {
                        Text("Play Again")
                    }
                },
            )
        }
    }
}

data class Square(val x: Float, val y: Float)
var arrayListNumbers = ArrayList<Int>()
fun generateInitialObstacles(pathWidth: Float, screenHeight: Float): List<Square> {
    return List(Random.nextInt(3)) { i ->
        val pathIndex = Random.nextInt(3)
        arrayListNumbers.add(pathIndex)
        Square(
            x = pathIndex * pathWidth + pathWidth / 2 - 20,
            y = i / 3 * 200f
        )
    }
}

var prevIndex = -1
fun generateRandomObstacle(pathWidth: Float, screenWidth: Float): Square {
    val arrayListNumbers = arrayListOf(0, 1, 2)
    val klm = arrayListOf(0, 0) // List of valid path indices
    var pathIndex: Int
    if (prevIndex == -1) {
        do {
            pathIndex = Random.nextInt(3)
        } while (pathIndex !in arrayListNumbers)
    } else {
        do {
            pathIndex = Random.nextInt(3)
        } while (pathIndex == prevIndex)
    }
    prevIndex = pathIndex
    return Square(
        x = pathIndex * pathWidth + pathWidth / 2 - 20,
        y = -40f
    )
}

fun isColliding(circlePos: Offset, squarePos: Square, circleRadius: Float, squareSize: Float): Boolean {
    val closestX = circlePos.x.coerceIn(squarePos.x, squarePos.x + squareSize)
    val closestY = circlePos.y.coerceIn(squarePos.y, squarePos.y + squareSize)
    val distanceX = circlePos.x - closestX
    val distanceY = circlePos.y - closestY
    return (distanceX * distanceX + distanceY * distanceY) < (circleRadius * circleRadius)
}*/
/*@Composable
fun MyCanvas() {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val context = LocalContext.current
    val jerryImageBitmap = ImageBitmap.imageResource(R.drawable.jerry)
    val tomImageBitmap = ImageBitmap.imageResource(R.drawable.tom)
    val screenWidth = with(density) { configuration.screenWidthDp.dp.toPx() }
    val screenHeight = with(density) { configuration.screenHeightDp.dp.toPx() }
    val pathWidth = screenWidth / 3
    val paths = listOf(pathWidth / 2, screenWidth / 2, screenWidth - pathWidth / 2)
    var circlePosition by remember {
        mutableStateOf(Offset(screenWidth / 2, screenHeight - 500f))
    }
    var gameEnd by remember { mutableStateOf(false) }
    var obstacles by remember { mutableStateOf(generateInitialObstacles(pathWidth, screenHeight)) }
    var score by remember { mutableIntStateOf(10 * (arrayListNumbers.size) + 10) }
    val mediaPlayer = remember { MediaPlayer.create(context, R.raw.jerryjump) }
    val mediaPlayer2 = remember { MediaPlayer.create(context, R.raw.bgsound) }
    var collisionCount by remember { mutableStateOf(0) }

    LaunchedEffect(gameEnd) {
        if (!gameEnd) {
            mediaPlayer2.start()
            while (!gameEnd) {
                delay(50)
                circlePosition = circlePosition.copy(y = circlePosition.y - 5f) // Move circle upwards
                obstacles = obstacles.map { it.copy(y = it.y + 15) }.filter { it.y < screenHeight + 40 }
                var spawnRate = 0.02
                var points = 10
                if (score in 100..249) {
                    spawnRate = 0.025
                    points = 15
                } else if (score >= 250) {
                    spawnRate = 0.03
                    points = 20
                }
                if (Random.nextFloat() < spawnRate) {
                    obstacles = obstacles + generateRandomObstacle(pathWidth, screenWidth)
                    score += points
                }

                if (circlePosition.y < 200f) {
                    circlePosition = Offset(screenWidth / 2, screenHeight - 900f) // Reset position
                }

                for (square in obstacles) {
                    if (isColliding(circlePosition, square, 50f, 40f)) {
                        collisionCount++
                        if (collisionCount >= 2) {
                            gameEnd = true
                            break
                        }
                    }
                }
            }
            mediaPlayer2.pause()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    if (offset.y < circlePosition.y) {
                        val nearestPathX =
                            paths.minByOrNull { abs(it - offset.x) } ?: (screenWidth / 2)
                        circlePosition = circlePosition.copy(x = nearestPathX)
                        mediaPlayer.start()
                    }
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Draw paths
            drawRect(color = Color(0xFFE5E5E5))
            val canvasWidth = size.width
            val canvasHeight = size.height
            val spacing = 37.dp.toPx()
            val rectWidth = 100.dp.toPx()
            val top = 0f
            for (i in 0 until 3) {
                val left = spacing + i * (rectWidth + spacing)
                drawRect(
                    color = Color(0xFF61C0BF),
                    topLeft = Offset(left, top),
                    size = Size(rectWidth, canvasHeight)
                )
            }

            for (square in obstacles) {
                drawRect(
                    color = Color.Blue,
                    topLeft = Offset(square.x, square.y),
                    size = Size(70f, 70f)
                )
            }

            if (gameEnd) {
                drawImage(
                    image = tomImageBitmap,
                    topLeft = Offset(
                        circlePosition.x - tomImageBitmap.width / 2,
                        circlePosition.y - tomImageBitmap.height / 2
                    ),
                    alpha = 1f
                )
            } else {
                val imageLeft1 = circlePosition.x - (tomImageBitmap.width / 2)
                val imageTop1 = screenHeight - tomImageBitmap.height

                drawImage(
                    image = tomImageBitmap,
                    topLeft = Offset(imageLeft1, imageTop1),
                    alpha = 1f
                )
            }

            val imageLeft = circlePosition.x - 370f / 2
            val imageTop = circlePosition.y - 200f / 2
            drawImage(
                image = jerryImageBitmap,
                topLeft = Offset(imageLeft, imageTop),
                alpha = 1f
            )
        }

        Text(
            text = "Score: $score",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        )

        if (gameEnd) {
            AlertDialog(
                onDismissRequest = {},
                title = { Text(text = "Game End") },
                text = { Text("Score is $score") },
                confirmButton = {
                    Button(onClick = {
                        gameEnd = false
                        circlePosition = Offset(screenWidth / 2, screenHeight - 100f)
                        obstacles = generateInitialObstacles(pathWidth, screenHeight)
                        score = 0
                        prevIndex = -1
                        arrayListNumbers = ArrayList<Int>()
                        collisionCount = 0 // Reset collision count
                    }, modifier = Modifier
                        .width(250.dp)  // Adjust width as needed
                        .height(40.dp)) {
                        Text("Play Again")
                    }
                },
            )
        }
    }
}

data class Square(val x: Float, val y: Float)
var arrayListNumbers = ArrayList<Int>()
fun generateInitialObstacles(pathWidth: Float, screenHeight: Float): List<Square> {
    return List(Random.nextInt(3)) { i ->
        val pathIndex = Random.nextInt(3)
        arrayListNumbers.add(pathIndex)
        Square(
            x = pathIndex * pathWidth + pathWidth / 2 - 20,
            y = i / 3 * 200f
        )
    }
}

var prevIndex = -1
fun generateRandomObstacle(pathWidth: Float, screenWidth: Float): Square {
    val arrayListNumbers = arrayListOf(0, 1, 2)
    val klm = arrayListOf(0, 0) // List of valid path indices
    var pathIndex: Int
    if (prevIndex == -1) {
        do {
            pathIndex = Random.nextInt(3)
        } while (pathIndex !in arrayListNumbers)
    } else {
        do {
            pathIndex = Random.nextInt(3)
        } while (pathIndex == prevIndex)
    }
    prevIndex = pathIndex
    return Square(
        x = pathIndex * pathWidth + pathWidth / 2 - 20,
        y = -40f
    )
}

fun isColliding(circlePos: Offset, squarePos: Square, circleRadius: Float, squareSize: Float): Boolean {
    val closestX = circlePos.x.coerceIn(squarePos.x, squarePos.x + squareSize)
    val closestY = circlePos.y.coerceIn(squarePos.y, squarePos.y + squareSize)
    val distanceX = circlePos.x - closestX
    val distanceY = circlePos.y - closestY
    return (distanceX * distanceX + distanceY * distanceY) < (circleRadius * circleRadius)
}*/
/*@Composable
fun MyCanvas() {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val context = LocalContext.current
    val jerryImageBitmap = ImageBitmap.imageResource(R.drawable.jerry)
    val tomImageBitmap = ImageBitmap.imageResource(R.drawable.tom)
    val screenWidth = with(density) { configuration.screenWidthDp.dp.toPx() }
    val screenHeight = with(density) { configuration.screenHeightDp.dp.toPx() }
    val pathWidth = screenWidth / 3
    val paths = listOf(pathWidth / 2, screenWidth / 2, screenWidth - pathWidth / 2)
    var circlePosition by remember { mutableStateOf(Offset(screenWidth / 2, screenHeight - 500f)) }
    var gameEnd by remember { mutableStateOf(false) }
    var obstacles by remember { mutableStateOf(generateInitialObstacles(pathWidth, screenHeight)) }
    var score by remember { mutableIntStateOf(10 * (arrayListNumbers.size) + 10) }
    val mediaPlayer = remember { MediaPlayer.create(context, R.raw.jerryjump) }
    val mediaPlayer2 = remember { MediaPlayer.create(context, R.raw.bgsound) }
    var collisionCount by remember { mutableStateOf(0) }

    LaunchedEffect(gameEnd) {
        if (!gameEnd) {
            mediaPlayer2.start()
            while (!gameEnd) {
                delay(50)
                circlePosition = circlePosition.copy(y = circlePosition.y - 5f) // Move circle upwards
                obstacles = obstacles.map { it.copy(y = it.y + 15) }.filter { it.y < screenHeight + 40 }
                var spawnRate = 0.02
                var points = 10
                if (score in 100..249) {
                    spawnRate = 0.025
                    points = 15
                } else if (score >= 250) {
                    spawnRate = 0.03
                    points = 20
                }
                if (Random.nextFloat() < spawnRate) {
                    obstacles = obstacles + generateRandomObstacle(pathWidth, screenWidth)
                    score += points
                }

                if (circlePosition.y < 200f) {
                    circlePosition = Offset(screenWidth / 2, screenHeight - 900f) // Reset position
                }

                for (square in obstacles) {
                    if (isColliding(circlePosition, square, 50f, 40f)) {
                        collisionCount++
                        if (collisionCount >= 2) {
                            gameEnd = true
                        }
                        break // Exit the loop after detecting a collision
                    }
                }
            }
            mediaPlayer2.pause()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    if (offset.y < circlePosition.y) {
                        val nearestPathX =
                            paths.minByOrNull { abs(it - offset.x) } ?: (screenWidth / 2)
                        circlePosition = circlePosition.copy(x = nearestPathX)
                        mediaPlayer.start()
                    }
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Draw paths
            drawRect(color = Color(0xFFE5E5E5))
            val canvasWidth = size.width
            val canvasHeight = size.height
            val spacing = 37.dp.toPx()
            val rectWidth = 100.dp.toPx()
            val top = 0f
            for (i in 0 until 3) {
                val left = spacing + i * (rectWidth + spacing)
                drawRect(
                    color = Color(0xFF61C0BF),
                    topLeft = Offset(left, top),
                    size = Size(rectWidth, canvasHeight)
                )
            }

            for (square in obstacles) {
                drawRect(
                    color = Color.Blue,
                    topLeft = Offset(square.x, square.y),
                    size = Size(70f, 70f)
                )
            }

            if (gameEnd) {
                drawImage(
                    image = tomImageBitmap,
                    topLeft = Offset(
                        circlePosition.x - tomImageBitmap.width / 2,
                        circlePosition.y - tomImageBitmap.height / 2
                    ),
                    alpha = 1f
                )
            } else {
                val imageLeft1 = circlePosition.x - (tomImageBitmap.width / 2)
                val imageTop1 = screenHeight - tomImageBitmap.height

                drawImage(
                    image = tomImageBitmap,
                    topLeft = Offset(imageLeft1, imageTop1),
                    alpha = 1f
                )
            }

            val imageLeft = circlePosition.x - 370f / 2
            val imageTop = circlePosition.y - 200f / 2
            drawImage(
                image = jerryImageBitmap,
                topLeft = Offset(imageLeft, imageTop),
                alpha = 1f
            )
        }

        Text(
            text = "Score: $score",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        )

        if (gameEnd) {
            AlertDialog(
                onDismissRequest = {},
                title = { Text(text = "Game End") },
                text = { Text("Score is $score") },
                confirmButton = {
                    Button(onClick = {
                        gameEnd = false
                        circlePosition = Offset(screenWidth / 2, screenHeight - 100f)
                        obstacles = generateInitialObstacles(pathWidth, screenHeight)
                        score = 0
                        prevIndex = -1
                        arrayListNumbers = ArrayList<Int>()
                        collisionCount = 0 // Reset collision count
                    }, modifier = Modifier
                        .width(250.dp)  // Adjust width as needed
                        .height(40.dp)) {
                        Text("Play Again")
                    }
                },
            )
        }
    }
}

data class Square(val x: Float, val y: Float)
var arrayListNumbers = ArrayList<Int>()
fun generateInitialObstacles(pathWidth: Float, screenHeight: Float): List<Square> {
    return List(Random.nextInt(3)) { i ->
        val pathIndex = Random.nextInt(3)
        arrayListNumbers.add(pathIndex)
        Square(
            x = pathIndex * pathWidth + pathWidth / 2 - 20,
            y = i / 3 * 200f
        )
    }
}

var prevIndex = -1
fun generateRandomObstacle(pathWidth: Float, screenWidth: Float): Square {
    val arrayListNumbers = arrayListOf(0, 1, 2)
    val klm = arrayListOf(0, 0) // List of valid path indices
    var pathIndex: Int
    if (prevIndex == -1) {
        do {
            pathIndex = Random.nextInt(3)
        } while (pathIndex !in arrayListNumbers)
    } else {
        do {
            pathIndex = Random.nextInt(3)
        } while (pathIndex == prevIndex)
    }
    prevIndex = pathIndex
    return Square(
        x = pathIndex * pathWidth + pathWidth / 2 - 20,
        y = -40f
    )
}

fun isColliding(circlePos: Offset, squarePos: Square, circleRadius: Float, squareSize: Float): Boolean {
    val closestX = circlePos.x.coerceIn(squarePos.x, squarePos.x + squareSize)
    val closestY = circlePos.y.coerceIn(squarePos.y, squarePos.y + squareSize)
    val distanceX = circlePos.x - closestX
    val distanceY = circlePos.y - closestY
    return (distanceX * distanceX + distanceY * distanceY) < (circleRadius * circleRadius)
}*/
/*@Composable
fun MyCanvas() {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val context = LocalContext.current
    val jerryImageBitmap = ImageBitmap.imageResource(R.drawable.jerry)
    val tomImageBitmap = ImageBitmap.imageResource(R.drawable.tom)
    val screenWidth = with(density) { configuration.screenWidthDp.dp.toPx() }
    val screenHeight = with(density) { configuration.screenHeightDp.dp.toPx() }
    val pathWidth = screenWidth / 3
    val paths = listOf(pathWidth / 2, screenWidth / 2, screenWidth - pathWidth / 2)
    var circlePosition by remember { mutableStateOf(Offset(screenWidth / 2, screenHeight - 500f)) }
    var gameEnd by remember { mutableStateOf(false) }
    var obstacles by remember { mutableStateOf(generateInitialObstacles(pathWidth, screenHeight)) }
    var score by remember { mutableIntStateOf(10 * (arrayListNumbers.size) + 10) }
    val mediaPlayer = remember { MediaPlayer.create(context, R.raw.jerryjump) }
    val mediaPlayer2 = remember { MediaPlayer.create(context, R.raw.bgsound) }
    var collisionCount by remember { mutableStateOf(0) }

    LaunchedEffect(gameEnd) {
        if (!gameEnd) {
            mediaPlayer2.start()
            while (!gameEnd) {
                delay(50)
                circlePosition = circlePosition.copy(y = circlePosition.y - 5f) // Move circle upwards
                obstacles = obstacles.map { it.copy(y = it.y + 15) }.filter { it.y < screenHeight + 40 }
                var spawnRate = 0.02
                var points = 10
                if (score in 100..249) {
                    spawnRate = 0.025
                    points = 15
                } else if (score >= 250) {
                    spawnRate = 0.03
                    points = 20
                }
                if (Random.nextFloat() < spawnRate) {
                    obstacles = obstacles + generateRandomObstacle(pathWidth, screenWidth)
                    score += points
                }

                if (circlePosition.y < 200f) {
                    circlePosition = Offset(screenWidth / 2, screenHeight - 900f) // Reset position
                }

                for (square in obstacles) {
                    if (isColliding(circlePosition, square, 50f, 40f)) {
                        collisionCount++
                        if (collisionCount >= 2) {
                            gameEnd = true
                        }
                        break // Exit the loop after detecting a collision
                    }
                }
            }
            mediaPlayer2.pause()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    if (offset.y < circlePosition.y) {
                        val nearestPathX =
                            paths.minByOrNull { abs(it - offset.x) } ?: (screenWidth / 2)
                        circlePosition = circlePosition.copy(x = nearestPathX)
                        mediaPlayer.start()
                    }
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Draw paths
            drawRect(color = Color(0xFFE5E5E5))
            val canvasWidth = size.width
            val canvasHeight = size.height
            val spacing = 37.dp.toPx()
            val rectWidth = 100.dp.toPx()
            val top = 0f
            for (i in 0 until 3) {
                val left = spacing + i * (rectWidth + spacing)
                drawRect(
                    color = Color(0xFF61C0BF),
                    topLeft = Offset(left, top),
                    size = Size(rectWidth, canvasHeight)
                )
            }

            for (square in obstacles) {
                drawRect(
                    color = Color.Blue,
                    topLeft = Offset(square.x, square.y),
                    size = Size(70f, 70f)
                )
            }

            if (gameEnd) {
                drawImage(
                    image = tomImageBitmap,
                    topLeft = Offset(
                        circlePosition.x - tomImageBitmap.width / 2,
                        circlePosition.y - tomImageBitmap.height / 2
                    ),
                    alpha = 1f
                )
            } else {
                val imageLeft1 = circlePosition.x - (tomImageBitmap.width / 2)
                val imageTop1 = screenHeight - tomImageBitmap.height

                drawImage(
                    image = tomImageBitmap,
                    topLeft = Offset(imageLeft1, imageTop1),
                    alpha = 1f
                )
            }

            val imageLeft = circlePosition.x - 370f / 2
            val imageTop = circlePosition.y - 200f / 2
            drawImage(
                image = jerryImageBitmap,
                topLeft = Offset(imageLeft, imageTop),
                alpha = 1f
            )
        }

        Text(
            text = "Score: $score",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        )

        if (gameEnd) {
            AlertDialog(
                onDismissRequest = {},
                title = { Text(text = "Game End") },
                text = { Text("Score is $score") },
                confirmButton = {
                    Button(onClick = {
                        gameEnd = false
                        circlePosition = Offset(screenWidth / 2, screenHeight - 100f)
                        obstacles = generateInitialObstacles(pathWidth, screenHeight)
                        score = 0
                        prevIndex = -1
                        arrayListNumbers = ArrayList<Int>()
                        collisionCount = 0 // Reset collision count
                    }, modifier = Modifier
                        .width(250.dp)  // Adjust width as needed
                        .height(40.dp)) {
                        Text("Play Again")
                    }
                },
            )
        }
    }
}

data class Square(val x: Float, val y: Float)
var arrayListNumbers = ArrayList<Int>()
fun generateInitialObstacles(pathWidth: Float, screenHeight: Float): List<Square> {
    return List(Random.nextInt(3)) { i ->
        val pathIndex = Random.nextInt(3)
        arrayListNumbers.add(pathIndex)
        Square(
            x = pathIndex * pathWidth + pathWidth / 2 - 20,
            y = i / 3 * 200f
        )
    }
}

var prevIndex = -1
fun generateRandomObstacle(pathWidth: Float, screenWidth: Float): Square {
    val arrayListNumbers = arrayListOf(0, 1, 2)
    var pathIndex: Int
    if (prevIndex == -1) {
        do {
            pathIndex = Random.nextInt(3)
        } while (pathIndex !in arrayListNumbers)
    } else {
        do {
            pathIndex = Random.nextInt(3)
        } while (pathIndex == prevIndex)
    }
    prevIndex = pathIndex
    return Square(
        x = pathIndex * pathWidth + pathWidth / 2 - 20,
        y = -40f
    )
}

fun isColliding(circlePos: Offset, squarePos: Square, circleRadius: Float, squareSize: Float): Boolean {
    val closestX = circlePos.x.coerceIn(squarePos.x, squarePos.x + squareSize)
    val closestY = circlePos.y.coerceIn(squarePos.y, squarePos.y + squareSize)
    val distanceX = circlePos.x - closestX
    val distanceY = circlePos.y - closestY
    return (distanceX * distanceX + distanceY * distanceY) < (circleRadius * circleRadius)
}*/
/*@Composable
fun MyCanvas() {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val context = LocalContext.current
    val jerryImageBitmap = ImageBitmap.imageResource(R.drawable.jerry)
    val tomImageBitmap = ImageBitmap.imageResource(R.drawable.tom)
    val screenWidth = with(density) { configuration.screenWidthDp.dp.toPx() }
    val screenHeight = with(density) { configuration.screenHeightDp.dp.toPx() }
    val pathWidth = screenWidth / 3
    val paths = listOf(pathWidth / 2, screenWidth / 2, screenWidth - pathWidth / 2)
    var circlePosition by remember { mutableStateOf(Offset(screenWidth / 2, screenHeight - 500f)) }
    var gameEnd by remember { mutableStateOf(false) }
    var obstacles by remember { mutableStateOf(generateInitialObstacles(pathWidth, screenHeight)) }
    var score by remember { mutableIntStateOf(10 * (arrayListNumbers.size) + 10) }
    val mediaPlayer = remember { MediaPlayer.create(context, R.raw.jerryjump) }
    val mediaPlayer2 = remember { MediaPlayer.create(context, R.raw.bgsound) }
    var collisionCount by remember { mutableStateOf(0) }

    LaunchedEffect(gameEnd) {
        if (!gameEnd) {
            mediaPlayer2.start()
            while (!gameEnd) {
                delay(50)
                circlePosition = circlePosition.copy(y = circlePosition.y - 5f) // Move circle upwards
                obstacles = obstacles.map { it.copy(y = it.y + 15) }.filter { it.y < screenHeight + 40 }
                var spawnRate = 0.02
                var points = 10
                if (score in 100..249) {
                    spawnRate = 0.025
                    points = 15
                } else if (score >= 250) {
                    spawnRate = 0.03
                    points = 20
                }
                if (Random.nextFloat() < spawnRate) {
                    obstacles = obstacles + generateRandomObstacle(pathWidth, screenWidth)
                    score += points
                }

                if (circlePosition.y < 200f) {
                    circlePosition = Offset(screenWidth / 2, screenHeight - 900f) // Reset position
                }

                var collided = false
                for (square in obstacles) {
                    if (isColliding(circlePosition, square, 50f, 40f)) {
                        collided = true
                        break
                    }
                }

                if (collided) {
                    collisionCount++
                    if (collisionCount >= 2) {
                        gameEnd = true
                    }
                }
            }
            mediaPlayer2.pause()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    if (offset.y < circlePosition.y) {
                        val nearestPathX = paths.minByOrNull { abs(it - offset.x) } ?: (screenWidth / 2)
                        circlePosition = circlePosition.copy(x = nearestPathX)
                        mediaPlayer.start()
                    }
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Draw paths
            drawRect(color = Color(0xFFE5E5E5))
            val canvasWidth = size.width
            val canvasHeight = size.height
            val spacing = 37.dp.toPx()
            val rectWidth = 100.dp.toPx()
            val top = 0f
            for (i in 0 until 3) {
                val left = spacing + i * (rectWidth + spacing)
                drawRect(
                    color = Color(0xFF61C0BF),
                    topLeft = Offset(left, top),
                    size = Size(rectWidth, canvasHeight)
                )
            }

            for (square in obstacles) {
                drawRect(
                    color = Color.Blue,
                    topLeft = Offset(square.x, square.y),
                    size = Size(70f, 70f)
                )
            }

            if (gameEnd) {
                drawImage(
                    image = tomImageBitmap,
                    topLeft = Offset(
                        circlePosition.x - tomImageBitmap.width / 2,
                        circlePosition.y - tomImageBitmap.height / 2
                    ),
                    alpha = 1f
                )
            } else {
                val imageLeft1 = circlePosition.x - (tomImageBitmap.width / 2)
                val imageTop1 = screenHeight - tomImageBitmap.height

                drawImage(
                    image = tomImageBitmap,
                    topLeft = Offset(imageLeft1, imageTop1),
                    alpha = 1f
                )
            }

            val imageLeft = circlePosition.x - 370f / 2
            val imageTop = circlePosition.y - 200f / 2
            drawImage(
                image = jerryImageBitmap,
                topLeft = Offset(imageLeft, imageTop),
                alpha = 1f
            )
        }

        Text(
            text = "Score: $score",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        )

        if (gameEnd) {
            AlertDialog(
                onDismissRequest = {},
                title = { Text(text = "Game End") },
                text = { Text("Score is $score") },
                confirmButton = {
                    Button(onClick = {
                        gameEnd = false
                        circlePosition = Offset(screenWidth / 2, screenHeight - 100f)
                        obstacles = generateInitialObstacles(pathWidth, screenHeight)
                        score = 0
                        prevIndex = -1
                        arrayListNumbers = ArrayList<Int>()
                        collisionCount = 0 // Reset collision count
                    }, modifier = Modifier
                        .width(250.dp)  // Adjust width as needed
                        .height(40.dp)) {
                        Text("Play Again")
                    }
                },
            )
        }
    }
}

data class Square(val x: Float, val y: Float)
var arrayListNumbers = ArrayList<Int>()
fun generateInitialObstacles(pathWidth: Float, screenHeight: Float): List<Square> {
    return List(Random.nextInt(3)) { i ->
        val pathIndex = Random.nextInt(3)
        arrayListNumbers.add(pathIndex)
        Square(
            x = pathIndex * pathWidth + pathWidth / 2 - 20,
            y = i / 3 * 200f
        )
    }
}

var prevIndex = -1
fun generateRandomObstacle(pathWidth: Float, screenWidth: Float): Square {
    val arrayListNumbers = arrayListOf(0, 1, 2)
    var pathIndex: Int
    if (prevIndex == -1) {
        do {
            pathIndex = Random.nextInt(3)
        } while (pathIndex !in arrayListNumbers)
    } else {
        do {
            pathIndex = Random.nextInt(3)
        } while (pathIndex == prevIndex)
    }
    prevIndex = pathIndex
    return Square(
        x = pathIndex * pathWidth + pathWidth / 2 - 20,
        y = -40f
    )
}

fun isColliding(circlePos: Offset, squarePos: Square, circleRadius: Float, squareSize: Float): Boolean {
                    val closestX = circlePos.x.coerceIn(squarePos.x, squarePos.x + squareSize)
                    val closestY = circlePos.y.coerceIn(squarePos.y, squarePos.y + squareSize)
                    val distanceX = circlePos.x - closestX
                    val distanceY = circlePos.y - closestY
                    return (distanceX * distanceX + distanceY * distanceY) < (circleRadius * circleRadius)
                }*/
@Composable
fun MyCanvas() {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val context = LocalContext.current
    val jerryImageBitmap = ImageBitmap.imageResource(R.drawable.jerry)
    val tomImageBitmap = ImageBitmap.imageResource(R.drawable.tom)
    val screenWidth = with(density) { configuration.screenWidthDp.dp.toPx() }
    val screenHeight = with(density) { configuration.screenHeightDp.dp.toPx() }
    val pathWidth = screenWidth / 3
    val paths = listOf(pathWidth / 2, screenWidth / 2, screenWidth - pathWidth / 2)
    var circlePosition by remember { mutableStateOf(Offset(screenWidth / 2, screenHeight - 500f)) }
    var gameEnd by remember { mutableStateOf(false) }
    var obstacles by remember { mutableStateOf(generateInitialObstacles(pathWidth, screenHeight)) }
    var score by remember { mutableIntStateOf(10 * (arrayListNumbers.size) + 10) }
    val mediaPlayer = remember { MediaPlayer.create(context, R.raw.jerryjump) }
    val mediaPlayer2 = remember { MediaPlayer.create(context, R.raw.bgsound) }
    var collisionCount by remember { mutableIntStateOf(0) }

    LaunchedEffect(gameEnd) {
        if (!gameEnd) {
            mediaPlayer2.start()
            while (!gameEnd) {
                delay(50)
                circlePosition = circlePosition.copy(y = circlePosition.y - 5f) // Move circle upwards
                obstacles = obstacles.map { it.copy(y = it.y + 15) }.filter { it.y < screenHeight + 40 }
                var spawnRate = 0.02
                var points = 10
                if (score in 100..249) {
                    spawnRate = 0.025
                    points = 15
                } else if (score >= 250) {
                    spawnRate = 0.03
                    points = 20
                }
                if (Random.nextFloat() < spawnRate) {
                    obstacles = obstacles + generateRandomObstacle(pathWidth, screenWidth)
                    score += points
                }

                if (circlePosition.y < 200f) {
                    circlePosition = Offset(screenWidth / 2, screenHeight - 900f) // Reset position
                }

                var collided = false
                for (square in obstacles) {
                    if (isColliding(circlePosition, square, 50f, 70f)) { // Adjusted collision detection size
                        collided = true
                        break
                    }
                }

                if (collided) {
                    collisionCount++
                    if (collisionCount >= 2) {
                        gameEnd = true
                        mediaPlayer2.pause() // Pause background sound on game end
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    if (offset.y < circlePosition.y) {
                        val nearestPathX = paths.minByOrNull { abs(it - offset.x) } ?: (screenWidth / 2)
                        circlePosition = circlePosition.copy(x = nearestPathX)
                        mediaPlayer.start()
                    }
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Draw paths
            drawRect(color = Color(0xFFE5E5E5))
            val canvasWidth = size.width
            val canvasHeight = size.height
            val spacing = 37.dp.toPx()
            val rectWidth = 100.dp.toPx()
            val top = 0f
            for (i in 0 until 3) {
                val left = spacing + i * (rectWidth + spacing)
                drawRect(
                    color = Color(0xFF61C0BF),
                    topLeft = Offset(left, top),
                    size = Size(rectWidth, canvasHeight)
                )
            }

            for (square in obstacles) {
                drawRect(
                    color = Color.Blue,
                    topLeft = Offset(square.x, square.y),
                    size = Size(70f, 70f)
                )
            }

            if (gameEnd) {
                drawImage(
                    image = tomImageBitmap,
                    topLeft = Offset(
                        circlePosition.x - tomImageBitmap.width / 2,
                        circlePosition.y - tomImageBitmap.height / 2
                    ),
                    alpha = 1f
                )
            } else {
                val imageLeft1 = circlePosition.x - (tomImageBitmap.width / 2)
                val imageTop1 = screenHeight - tomImageBitmap.height

                drawImage(
                    image = tomImageBitmap,
                    topLeft = Offset(imageLeft1, imageTop1),
                    alpha = 1f
                )
            }

            val imageLeft = circlePosition.x - 370f / 2
            val imageTop = circlePosition.y - 200f / 2
            drawImage(
                image = jerryImageBitmap,
                topLeft = Offset(imageLeft, imageTop),
                alpha = 1f
            )
        }

        Text(
            text = "Score: $score",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        )

        if (gameEnd) {
            AlertDialog(
                onDismissRequest = {},
                title = { Text("Game End") },
                text = { Text("Score is $score") },
                confirmButton = {
                    Column {
                        /*Button(
                            onClick = {
                                //saveHighScore(score, context)
                                //showHighScores(context)
                            },
                            modifier = Modifier
                                .width(250.dp)
                                .height(40.dp)
                        ) {
                            Text("High Scores")
                        }*/
                        Button(
                            onClick = {
                                gameEnd = false
                                circlePosition = Offset(screenWidth / 2, screenHeight - 100f)
                                obstacles = generateInitialObstacles(pathWidth, screenHeight)
                                score = 0
                                prevIndex = -1
                                arrayListNumbers.clear()
                                collisionCount = 0 // Reset collision count
                                // Navigate to home screen (assuming MainActivity is your home screen)
                                context.startActivity(Intent(context, MainActivity::class.java).apply {
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                })
                            },
                            modifier = Modifier
                                .width(250.dp)
                                .height(40.dp)
                        ) {
                            Text("Home")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = {
                                gameEnd = false
                                circlePosition = Offset(screenWidth / 2, screenHeight - 100f)
                                obstacles = generateInitialObstacles(pathWidth, screenHeight)
                                score = 0
                                prevIndex = -1
                                arrayListNumbers.clear()
                                collisionCount = 0 // Reset collision count
                            }, modifier = Modifier
                                .width(250.dp)
                                .height(40.dp)) {
                                Text("Play Again")
                            }
                    }
                }
            )
        }
    }
}

data class Square(val x: Float, val y: Float)
var arrayListNumbers = ArrayList<Int>()
fun generateInitialObstacles(pathWidth: Float, screenHeight: Float): List<Square> {
    return List(Random.nextInt(3)) { i ->
        val pathIndex = Random.nextInt(3)
        arrayListNumbers.add(pathIndex)
        Square(
            x = pathIndex * pathWidth + pathWidth / 2 - 20,
            y = i / 3 * 200f
        )
    }
}

var prevIndex = -1
fun generateRandomObstacle(pathWidth: Float, screenWidth: Float): Square {
    val arrayListNumbers = arrayListOf(0, 1, 2)
    var pathIndex: Int
    if (prevIndex == -1) {
        do {
            pathIndex = Random.nextInt(3)
        } while (pathIndex !in arrayListNumbers)
    } else {
        do {
            pathIndex = Random.nextInt(3)
        } while (pathIndex == prevIndex)
    }
    prevIndex = pathIndex
    return Square(
        x = pathIndex * pathWidth + pathWidth / 2 - 20,
        y = -40f
    )
}

fun isColliding(circlePos: Offset, squarePos: Square, circleRadius: Float, squareSize: Float): Boolean {
    val closestX = circlePos.x.coerceIn(squarePos.x, squarePos.x + squareSize)
    val closestY = circlePos.y.coerceIn(squarePos.y, squarePos.y + squareSize)
    val distanceX = circlePos.x - closestX
    val distanceY = circlePos.y - closestY
    return (distanceX * distanceX + distanceY * distanceY) < (circleRadius * circleRadius)
}
/*fun saveHighScore(score: Int, context: Context) {
    val prefs = context.getSharedPreferences("MyGamePrefs", Context.MODE_PRIVATE)
    val scores = prefs.getStringSet("highScores", mutableSetOf())?.toMutableSet() ?: mutableSetOf()
    scores.add(score.toString())
    prefs.edit().putStringSet("highScores", scores).apply()
}

fun showHighScores(context: Context) {
    val prefs = context.getSharedPreferences("MyGamePrefs", Context.MODE_PRIVATE)
    val scores = prefs.getStringSet("highScores", mutableSetOf())?.map { it.toInt() }?.sortedDescending()

    // Display the scores (for example, in a dialog or a list)
    AlertDialog(
        onDismissRequest = {},
        title = { Text("High Scores") },
        text = {
            Column {
                scores?.take(10)?.forEachIndexed { index, score ->
                    Text("$index. $score")
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { /* Dismiss dialog or navigate back */ },
                modifier = Modifier.width(250.dp).height(40.dp)
            ) {
                Text("OK")
            }
        }
    )
}


*/