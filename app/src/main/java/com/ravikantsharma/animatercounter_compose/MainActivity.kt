package com.ravikantsharma.animatercounter_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ravikantsharma.animatercounter_compose.ui.theme.AnimaterCounterComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnimaterCounterComposeTheme {
                Counter()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CounterView() {
    Counter()
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Counter() {
    var count by remember {
        mutableStateOf(0)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CounterButton("+") { count++ }

        Row(
            modifier = Modifier
                .animateContentSize()
                .padding(horizontal = 32.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            count.toString().reversed()
                .mapIndexed { index, c ->
                    Digit(c, count, index)
                }
                .reversed()
                .forEach { digit ->
                    AnimatedContent(
                        targetState = digit,
                        transitionSpec = {
                            if (targetState > initialState) {
                                slideInVertically { -it } with slideOutVertically { it }
                            } else {
                                slideInVertically { it } with slideOutVertically { -it }
                            }
                        }
                    ) { digit ->
                        Text(
                            text = "${digit.digitChar}",
                            style = MaterialTheme.typography.h1,
                            textAlign = TextAlign.Center
                        )
                    }
                }
        }

        CounterButton("-") { count-- }
    }
}

@Composable
fun CounterButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .size(70.dp)
            .clip(CircleShape),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.h4,
            textAlign = TextAlign.Center
        )
    }
}

data class Digit(val digitChar: Char, val fullNumber: Int, val place: Int) {
    override fun equals(other: Any?): Boolean {
        return when (other) {
            is Digit -> digitChar == other.digitChar
            else -> super.equals(other)
        }
    }
}

operator fun Digit.compareTo(other: Digit): Int {
    return fullNumber.compareTo(other.fullNumber)
}